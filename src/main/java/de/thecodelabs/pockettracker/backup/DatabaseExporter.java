package de.thecodelabs.pockettracker.backup;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.backup.converter.ShowConverter;
import de.thecodelabs.pockettracker.backup.converter.UserConverter;
import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.BackupUserModel;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DatabaseExporter
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseExporter.class);

	private final ShowRepository showRepository;
	private final UserRepository userRepository;

	private final ShowConverter showConverter;
	private final UserConverter userConverter;

	private final ObjectMapper objectMapper;

	@Autowired
	public DatabaseExporter(ShowRepository showRepository, UserRepository userRepository, ShowConverter showConverter, UserConverter userConverter, ObjectMapper objectMapper)
	{
		this.showRepository = showRepository;
		this.userRepository = userRepository;
		this.showConverter = showConverter;
		this.userConverter = userConverter;
		this.objectMapper = objectMapper;
	}

	public void export() throws IOException
	{
		LOGGER.info("Exporting database...");

		final List<Show> shows = showRepository.findAll();
		final List<BackupShowModel> backupShowModels = showConverter.toBeans(shows);

		final List<User> users = userRepository.findAll();
		final List<BackupUserModel> backupUserModels = userConverter.toBeans(users);

		final Database database = new Database(backupShowModels, backupUserModels);

		objectMapper.writerWithDefaultPrettyPrinter().writeValue(System.out, database);

		LOGGER.info("Export done");
	}
}
