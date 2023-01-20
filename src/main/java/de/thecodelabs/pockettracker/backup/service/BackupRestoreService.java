package de.thecodelabs.pockettracker.backup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.administration.apiconfiguration.APIConfigurationService;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.backup.converter.APIConfigurationConverter;
import de.thecodelabs.pockettracker.backup.converter.ShowConverter;
import de.thecodelabs.pockettracker.backup.converter.user.UserConverter;
import de.thecodelabs.pockettracker.backup.model.BackupAPIConfigurationModel;
import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.Database;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowImageType;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BackupRestoreService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupRestoreService.class);

	private final UserService userService;
	private final ShowService showService;
	private final EpisodeService episodeService;
	private final APIConfigurationService apiConfigurationService;

	private final DataSource dataSource;

	private final ShowConverter showConverter;
	private final UserConverter userConverter;
	private final APIConfigurationConverter apiConfigurationConverter;

	private final ObjectMapper objectMapper;

	private final WebConfigurationProperties webConfigurationProperties;

	@Autowired
	public BackupRestoreService(UserService userService, ShowService showService, EpisodeService episodeService,
								APIConfigurationService apiConfigurationService, DataSource dataSource, ShowConverter showConverter,
								UserConverter userConverter, APIConfigurationConverter apiConfigurationConverter, ObjectMapper objectMapper, WebConfigurationProperties webConfigurationProperties)
	{
		this.userService = userService;
		this.showService = showService;
		this.episodeService = episodeService;
		this.apiConfigurationService = apiConfigurationService;
		this.dataSource = dataSource;
		this.showConverter = showConverter;
		this.userConverter = userConverter;
		this.apiConfigurationConverter = apiConfigurationConverter;
		this.objectMapper = objectMapper;
		this.webConfigurationProperties = webConfigurationProperties;
	}

	public void clearDatabase()
	{
		final List<User> users = userService.getUsers();
		LOGGER.info("Delete {} users", users.size());

		for(User user : users)
		{
			userService.deleteUser(user);
		}

		final List<Show> shows = showService.getAllShows(null);
		LOGGER.info("Delete {} shows", shows.size());
		for(Show show : shows)
		{
			for(ShowImageType type : ShowImageType.values())
			{
				showService.deleteShowImage(type, show);
			}
			showService.deleteShow(show);
		}
	}

	public void insertAllData(Path basePath) throws IOException, SQLException
	{
		final Path databasePath = basePath.resolve(DATABASE_PATH_NAME);
		final BufferedReader bufferedReader = Files.newBufferedReader(databasePath);

		final Database database = objectMapper.reader().readValue(bufferedReader, Database.class);
		insertShows(database.shows());
		insertUsers(database.users());
		insertApiConfigurations(database.apiConfigurations());

		updateSequences();
	}

	public void insertShows(List<BackupShowModel> backupShowModels)
	{
		final List<Show> shows = showConverter.toEntities(backupShowModels);
		showService.createShows(shows);
		LOGGER.info("Restored shows");
	}

	public void insertUsers(List<BackupUserModel> backupUserModels)
	{
		final List<User> users = userConverter.toEntities(backupUserModels);
		for(User user : users)
		{
			userService.createUser(user);
		}
		LOGGER.info("Restored users");

		for(User user : userService.getUsers())
		{
			final Optional<BackupUserModel> backupUserModelOptional = backupUserModels.stream().filter(u -> u.getId().equals(user.getId())).findFirst();
			if(backupUserModelOptional.isPresent())
			{
				final BackupUserModel backupUserModel = backupUserModelOptional.get();
				user.addShows(backupUserModel.getShows().stream()
						.map(model -> {
							final Optional<Show> showOptional = showService.getShowById(model.getShowId());
							if(showOptional.isEmpty())
							{
								return Optional.<AddedShow>empty();
							}
							return Optional.of(new AddedShow(user, showOptional.get(), model.getDisliked()));
						})
						.filter(Optional::isPresent)
						.map(Optional::get)
						.toList());

				user.addWatchedEpisodes(backupUserModel.getWatchedEpisodes().stream().map(backupWatchedEpisodeModel -> {
					final Optional<Episode> episodeOptional = episodeService.getEpisodeById(backupWatchedEpisodeModel.getEpisodeId());
					if(episodeOptional.isEmpty())
					{
						LOGGER.warn(MessageFormat.format("No episode found for id: {0}", backupWatchedEpisodeModel.getEpisodeId()));
						return null;
					}
					return new WatchedEpisode(user, episodeOptional.get(), backupWatchedEpisodeModel.getWatchedAt());
				}).filter(Objects::nonNull).toList());
				userService.saveUser(user);
			}
		}
		LOGGER.info("Restored user shows and episodes");
	}

	public void insertApiConfigurations(List<BackupAPIConfigurationModel> backupAPIConfigurationModels)
	{
		final List<APIConfiguration> apiConfigurations = apiConfigurationConverter.toEntities(backupAPIConfigurationModels);
		for(APIConfiguration apiConfiguration : apiConfigurations)
		{
			apiConfigurationService.createConfiguration(apiConfiguration);
		}
		LOGGER.info("Restored API configurations");
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
								LOGGER.info("Set sequence {} to value {}", sequence, targetValue);
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
			LOGGER.info("Backup does not contain images");
			return;
		}

		final Path targetPath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());

		FileUtils.copyDirectory(backupPath.toFile(), targetPath.toFile());
		LOGGER.info("Restored image resources to {}", targetPath);
	}
}
