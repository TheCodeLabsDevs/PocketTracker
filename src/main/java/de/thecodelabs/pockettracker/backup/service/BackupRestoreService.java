package de.thecodelabs.pockettracker.backup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.backup.converter.ShowConverter;
import de.thecodelabs.pockettracker.backup.converter.user.UserConverter;
import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.Database;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowImageType;
import de.thecodelabs.pockettracker.user.model.User;
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
import java.util.List;
import java.util.stream.Collectors;

import static de.thecodelabs.pockettracker.backup.service.BackupService.DATABASE_PATH_NAME;

@Service
public class BackupRestoreService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupRestoreService.class);

	private final UserService userService;
	private final ShowService showService;

	private final DataSource dataSource;
	private final EntityManager entityManager;

	private final ShowConverter showConverter;
	private final UserConverter userConverter;

	private final ObjectMapper objectMapper;

	@Autowired
	public BackupRestoreService(UserService userService, ShowService showService, DataSource dataSource,
								EntityManager entityManager, ShowConverter showConverter, UserConverter userConverter,
								ObjectMapper objectMapper)
	{
		this.userService = userService;
		this.showService = showService;
		this.dataSource = dataSource;
		this.entityManager = entityManager;
		this.showConverter = showConverter;
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

	private void insertShows(List<BackupShowModel> backupShowModels)
	{
		final List<Show> shows = showConverter.toEntities(backupShowModels);
		for(Show show : shows)
		{
			showService.createShow(show);
		}
	}

	private void insertUsers(List<BackupUserModel> backupUserModels)
	{
		final List<User> users = userConverter.toEntities(backupUserModels);
		for(User user : users)
		{
			userService.createUser(user);
		}
	}
}
