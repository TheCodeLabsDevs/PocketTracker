package de.thecodelabs.pockettracker.backup;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.backup.configuration.BackupConfigurationProperties;
import de.thecodelabs.pockettracker.backup.converter.ShowConverter;
import de.thecodelabs.pockettracker.backup.converter.UserConverter;
import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.BackupUserModel;
import de.thecodelabs.pockettracker.backup.model.Database;
import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PocketTrackerExportService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PocketTrackerExportService.class);

	private final ShowRepository showRepository;
	private final UserRepository userRepository;

	private final ShowConverter showConverter;
	private final UserConverter userConverter;

	private final WebConfigurationProperties webConfigurationProperties;
	private final BackupConfigurationProperties backupConfigurationProperties;

	private final ObjectMapper objectMapper;

	@Autowired
	public PocketTrackerExportService(ShowRepository showRepository, UserRepository userRepository, ShowConverter showConverter,
									  UserConverter userConverter, WebConfigurationProperties webConfigurationProperties,
									  BackupConfigurationProperties backupConfigurationProperties, ObjectMapper objectMapper)
	{
		this.showRepository = showRepository;
		this.userRepository = userRepository;
		this.showConverter = showConverter;
		this.userConverter = userConverter;
		this.webConfigurationProperties = webConfigurationProperties;
		this.backupConfigurationProperties = backupConfigurationProperties;
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

		final String backupFolderName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		final Path backupLocationPath = Paths.get(backupConfigurationProperties.getLocation(), backupFolderName);
		if(Files.notExists(backupLocationPath))
		{
			Files.createDirectories(backupLocationPath);
		}

		exportDatabase(backupLocationPath, database);
		exportImages(backupLocationPath);

		LOGGER.info("Export done");
	}

	private void exportImages(Path basePath) throws IOException
	{
		final Path imagesSourcePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());
		final Path imageBackupPath = basePath.resolve("images");

		FileUtils.copyDirectory(imagesSourcePath.toFile(), imageBackupPath.toFile());
	}

	private void exportDatabase(Path basePath, Database database) throws IOException
	{
		final Path databasePath = basePath.resolve("database.json");
		final BufferedWriter writer = Files.newBufferedWriter(databasePath);

		objectMapper.writer().writeValue(writer, database);
	}
}
