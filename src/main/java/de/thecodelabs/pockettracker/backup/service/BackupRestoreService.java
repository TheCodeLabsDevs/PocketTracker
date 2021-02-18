package de.thecodelabs.pockettracker.backup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.backup.converter.EpisodeConverter;
import de.thecodelabs.pockettracker.backup.converter.SeasonConverter;
import de.thecodelabs.pockettracker.backup.converter.ShowConverter;
import de.thecodelabs.pockettracker.backup.converter.user.UserConverter;
import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.Database;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.service.SeasonService;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowImageType;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.thecodelabs.pockettracker.backup.service.BackupService.DATABASE_PATH_NAME;

@Service
public class BackupRestoreService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupRestoreService.class);

	private final UserService userService;
	private final ShowService showService;
	private final SeasonService seasonService;
	private final EpisodeService episodeService;

	private final DataSource dataSource;
	private final EntityManager entityManager;

	private final ShowConverter showConverter;
	private final SeasonConverter seasonConverter;
	private final EpisodeConverter episodeConverter;
	private final UserConverter userConverter;

	private final ObjectMapper objectMapper;

	@Autowired
	public BackupRestoreService(UserService userService, ShowService showService, SeasonService seasonService,
								EpisodeService episodeService, DataSource dataSource, EntityManager entityManager,
								ShowConverter showConverter, SeasonConverter seasonConverter, EpisodeConverter episodeConverter,
								UserConverter userConverter, ObjectMapper objectMapper)
	{
		this.userService = userService;
		this.showService = showService;
		this.seasonService = seasonService;
		this.episodeService = episodeService;
		this.dataSource = dataSource;
		this.entityManager = entityManager;
		this.showConverter = showConverter;
		this.seasonConverter = seasonConverter;
		this.episodeConverter = episodeConverter;
		this.userConverter = userConverter;
		this.objectMapper = objectMapper;
	}

	public void clearDatabase() throws SQLException
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

		resetSequences();
	}

	private void resetSequences() throws SQLException
	{
		try(final Connection connection = dataSource.getConnection())
		{
			final String productName = connection.getMetaData().getDatabaseProductName();
			if(productName.equals("PostgreSQL"))
			{
				final Query query = entityManager.createNativeQuery("SELECT c.relname FROM pg_class c WHERE c.relkind = 'S';");
				//noinspection unchecked
				final List<Object> resultList = (List<Object>) query.getResultList();
				final List<String> sequences = resultList.stream().map(Object::toString).collect(Collectors.toList());

				for(String sequence : sequences)
				{
					try(final Statement statement = connection.createStatement())
					{
						statement.execute("ALTER SEQUENCE " + sequence + " RESTART WITH 1;");
					}
				}
			}
		}
	}

	public void insertAllData(Path basePath) throws IOException
	{
		final Path databasePath = basePath.resolve(DATABASE_PATH_NAME);
		final BufferedReader bufferedReader = Files.newBufferedReader(databasePath);

		final Database database = objectMapper.reader().readValue(bufferedReader, Database.class);
		insertShows(database.getShows());
		insertUsers(database.getUsers());
	}

	public void insertShows(List<BackupShowModel> backupShowModels)
	{
		final List<Show> shows = showConverter.toEntities(backupShowModels);
		showService.createShows(shows);

		final List<Season> seasons = backupShowModels.stream()
				.flatMap(show -> {
					final List<Season> entities = seasonConverter.toEntities(show.getSeasons());
					return entities.stream().map(entity -> {
						final Optional<Show> showOptional = showService.getShowById(show.getId());
						if(showOptional.isEmpty())
						{
							return null;
						}
						entity.setShow(showOptional.get());
						return entity;
					}).filter(Objects::nonNull);
				})
				.sorted(Comparator.comparing(Season::getId))
				.collect(Collectors.toList());
		seasonService.createSeasons(seasons);

		final List<Episode> episodes = backupShowModels.stream()
				.flatMap(show -> show.getSeasons().stream())
				.flatMap(season -> {
					final List<Episode> entities = episodeConverter.toEntities(season.getEpisodes());
					return entities.stream().map(entity -> {
						final Optional<Season> seasonOptional = seasonService.getSeasonById(season.getId());
						if(seasonOptional.isEmpty())
						{
							return null;
						}
						entity.setSeason(seasonOptional.get());
						return entity;
					}).filter(Objects::nonNull);
				})
				.sorted(Comparator.comparing(Episode::getId))
				.collect(Collectors.toList());
		episodeService.createEpisodes(episodes);
	}

	public void insertUsers(List<BackupUserModel> backupUserModels)
	{
		final List<User> users = userConverter.toEntities(backupUserModels);
		for(User user : users)
		{
			userService.createUser(user);
		}

		for(User user : userService.getUsers())
		{
			final Optional<BackupUserModel> backupUserModelOptional = backupUserModels.stream().filter(u -> u.getId().equals(user.getId())).findFirst();
			if(backupUserModelOptional.isPresent())
			{
				final BackupUserModel backupUserModel = backupUserModelOptional.get();
				user.addShows(backupUserModel.getShows().stream().map(showService::getShowById)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.collect(Collectors.toList()));

				user.addWatchedEpisodes(backupUserModel.getWatchedEpisodes().stream().map(backupWatchedEpisodeModel -> {
					final Optional<Episode> episodeOptional = episodeService.getEpisodeById(backupWatchedEpisodeModel.getEpisodeId());
					if(episodeOptional.isEmpty())
					{
						LOGGER.warn("No episode found for id: " + backupWatchedEpisodeModel.getEpisodeId());
						return null;
					}
					return new WatchedEpisode(user, episodeOptional.get(), backupWatchedEpisodeModel.getWatchedAt());
				}).filter(Objects::nonNull).collect(Collectors.toList()));
				userService.saveUser(user);
			}
		}
	}
}
