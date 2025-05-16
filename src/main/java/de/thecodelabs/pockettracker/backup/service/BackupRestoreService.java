package de.thecodelabs.pockettracker.backup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.administration.apiconfiguration.APIConfigurationService;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.backup.converter.APIConfigurationConverter;
import de.thecodelabs.pockettracker.backup.converter.MovieConverter;
import de.thecodelabs.pockettracker.backup.converter.ShowConverter;
import de.thecodelabs.pockettracker.backup.converter.user.UserConverter;
import de.thecodelabs.pockettracker.backup.model.BackupAPIConfigurationModel;
import de.thecodelabs.pockettracker.backup.model.BackupMovieModel;
import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.Database;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import de.thecodelabs.pockettracker.mediaitem.MediaItemImageType;
import de.thecodelabs.pockettracker.movie.MovieService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.HiddenShow;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static de.thecodelabs.pockettracker.backup.service.BackupService.DATABASE_PATH_NAME;
import static de.thecodelabs.pockettracker.backup.service.BackupService.IMAGE_PATH_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupRestoreService
{
	private final UserService userService;
	private final ShowService showService;
	private final MovieService movieService;
	private final EpisodeService episodeService;
	private final APIConfigurationService apiConfigurationService;

	private final DataSource dataSource;

	private final ShowConverter showConverter;
	private final MovieConverter movieConverter;
	private final UserConverter userConverter;
	private final APIConfigurationConverter apiConfigurationConverter;

	private final ObjectMapper objectMapper;

	private final WebConfigurationProperties webConfigurationProperties;


	public void clearDatabase()
	{
		final List<User> users = userService.getUsers();
		log.info("Delete {} users", users.size());

		for(User user : users)
		{
			userService.deleteUser(user);
		}

		final List<Show> shows = showService.getAll(null);
		log.info("Delete {} shows", shows.size());
		for(Show show : shows)
		{
			for(MediaItemImageType type : MediaItemImageType.values())
			{
				showService.deleteImage(type, show);
			}
			showService.deleteItem(show);
		}

		final List<Movie> movies = movieService.getAll(null);
		log.info("Delete {} movies", movies.size());
		for(Movie movie : movies)
		{
			for(MediaItemImageType type : MediaItemImageType.values())
			{
				movieService.deleteImage(type, movie);
			}
			movieService.deleteItem(movie);
		}

		final List<APIConfiguration> configurations = apiConfigurationService.getAllConfigurations();
		log.info("Delete {} API configurations", configurations.size());

		for(APIConfiguration configuration : configurations)
		{
			apiConfigurationService.deleteConfiguration(configuration);
		}
	}

	public void insertAllData(Path basePath) throws IOException, SQLException
	{
		final Path databasePath = basePath.resolve(DATABASE_PATH_NAME);
		final BufferedReader bufferedReader = Files.newBufferedReader(databasePath);

		final Database database = objectMapper.reader().readValue(bufferedReader, Database.class);
		insertShows(database.shows());
		insertMovies(database.movies());
		insertUsers(database.users());
		insertApiConfigurations(database.apiConfigurations());

		updateSequences();
	}

	public void insertShows(List<BackupShowModel> backupShowModels)
	{
		final List<Show> shows = showConverter.toEntities(backupShowModels);
		showService.createAll(shows);
		log.info("Restored shows");
	}

	public void insertMovies(List<BackupMovieModel> backupMovieModels)
	{
		final List<Movie> movies = movieConverter.toEntities(backupMovieModels);
		movieService.createAll(movies);
		log.info("Restored movies");
	}

	public void insertUsers(List<BackupUserModel> backupUserModels)
	{
		final List<User> users = userConverter.toEntities(backupUserModels);
		for(User user : users)
		{
			userService.createUser(user);
		}
		log.info("Restored users");

		for(User user : userService.getUsers())
		{
			final Optional<BackupUserModel> backupUserModelOptional = backupUserModels.stream().filter(u -> u.getId().equals(user.getId())).findFirst();
			if(backupUserModelOptional.isPresent())
			{
				final BackupUserModel backupUserModel = backupUserModelOptional.get();
				user.addAllHiddenShows(backupUserModel.getHiddenShows().stream()
						.map(model -> {
							final Optional<Show> showOptional = showService.getById(model.getShowId());
							if(showOptional.isEmpty())
							{
								return Optional.<HiddenShow>empty();
							}
							return Optional.of(new HiddenShow(user, showOptional.get()));
						})
						.filter(Optional::isPresent)
						.map(Optional::get)
						.toList());

				user.addWatchedEpisodes(backupUserModel.getWatchedEpisodes().stream().map(backupWatchedEpisodeModel -> {
					final Optional<Episode> episodeOptional = episodeService.getEpisodeById(backupWatchedEpisodeModel.getEpisodeId());
					if(episodeOptional.isEmpty())
					{
						log.warn("No episode found for id: {}", backupWatchedEpisodeModel.getEpisodeId());
						return null;
					}
					return new WatchedEpisode(user, episodeOptional.get(), backupWatchedEpisodeModel.getWatchedAt());
				}).filter(Objects::nonNull).toList());

				user.addMovies(backupUserModel.getMovies().stream()
						.map(model -> {
							final Optional<Movie> movieOptional = movieService.getById(model.getMovieId());
							if(movieOptional.isEmpty())
							{
								return Optional.<AddedMovie>empty();
							}
							return Optional.of(new AddedMovie(user, movieOptional.get(), model.getWatchedDate()));
						})
						.filter(Optional::isPresent)
						.map(Optional::get)
						.toList());

				userService.saveUser(user);
			}
		}
		log.info("Restored user shows, episodes and movies");
	}

	public void insertApiConfigurations(List<BackupAPIConfigurationModel> backupAPIConfigurationModels)
	{
		final List<APIConfiguration> apiConfigurations = apiConfigurationConverter.toEntities(backupAPIConfigurationModels);
		for(APIConfiguration apiConfiguration : apiConfigurations)
		{
			apiConfigurationService.createConfiguration(apiConfiguration);
		}
		log.info("Restored API configurations");
	}

	private void updateSequences() throws SQLException
	{
		try(final Connection connection = dataSource.getConnection())
		{
			final String productName = connection.getMetaData().getDatabaseProductName();
			if(productName.equals("PostgreSQL"))
			{
				final String sqlStatement = """
						SELECT t.oid::regclass AS table_name,
						       a.attname AS column_name,
						       s.relname AS sequence_name
						FROM pg_class AS t
						   JOIN pg_attribute AS a
						      ON a.attrelid = t.oid
						   JOIN pg_depend AS d
						      ON d.refobjid = t.oid
						         AND d.refobjsubid = a.attnum
						   JOIN pg_class AS s
						      ON s.oid = d.objid
						WHERE d.classid = 'pg_catalog.pg_class'::regclass
						  AND d.refclassid = 'pg_catalog.pg_class'::regclass
						  AND d.deptype = 'i'
						  AND t.relkind IN ('r', 'P')
						  AND s.relkind = 'S';
						""";
				try(Statement queryStatement = connection.createStatement())
				{
					queryStatement.execute(sqlStatement);
					try(final ResultSet resultSet = queryStatement.getResultSet())
					{
						while(resultSet.next())
						{
							final String sequence = resultSet.getString("sequence_name");
							final String tableName = resultSet.getString("table_name");
							final String idColumn = resultSet.getString("column_name");

							final Long biggestId = getBiggestId(connection, tableName, idColumn);
							try(final Statement statement = connection.createStatement())
							{
								long targetValue = biggestId != null ? biggestId + 1 : 1;
								statement.execute(String.format("ALTER SEQUENCE %s RESTART WITH %d;", sequence, targetValue));
								log.info("Set sequence {} to value {}", sequence, targetValue);
							}
						}
					}
				}
			}
		}
	}

	private Long getBiggestId(Connection connection, String tableName, String idColumn) throws SQLException
	{
		try(final Statement statement = connection.createStatement())
		{
			statement.execute(String.format("SELECT max(%s) FROM %s", idColumn, tableName));
			try(final ResultSet resultSet = statement.getResultSet())
			{
				if(resultSet.next())
				{
					return resultSet.getLong(1);
				}
			}
		}
		return null;
	}

	public void copyImages(Path basePath) throws IOException
	{
		final Path backupPath = basePath.resolve(IMAGE_PATH_NAME);
		if(Files.notExists(backupPath))
		{
			log.info("Backup does not contain images");
			return;
		}

		final Path targetPath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());

		FileUtils.copyDirectory(backupPath.toFile(), targetPath.toFile());
		log.info("Restored image resources to {}", targetPath);
	}
}
