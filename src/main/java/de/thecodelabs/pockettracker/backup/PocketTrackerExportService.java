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
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Transactional
	public void export() throws IOException
	{
		LOGGER.info("Start backup...");

		final List<Show> shows = showRepository.findAll();
		final List<BackupShowModel> backupShowModels = showConverter.toBeans(shows);

		final List<User> users = userRepository.findAll();
		final List<BackupUserModel> backupUserModels = userConverter.toBeans(users);
		final Database database = new Database(backupShowModels, backupUserModels);

		final String backupFolderName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
		final Path basePath = Paths.get(backupConfigurationProperties.getLocation());

		final Path backupLocationPath = basePath.resolve(backupFolderName);
		if(Files.notExists(backupLocationPath))
		{
			Files.createDirectories(backupLocationPath);
		}

		LOGGER.info("Cleaning old backups...");
		deleteOldBackups(basePath);

		LOGGER.info("Create backup at {}", backupLocationPath);

		exportDatabase(backupLocationPath, database);
		exportImages(backupLocationPath);

		LOGGER.info("Backup done");
	}

	private void deleteOldBackups(Path backupLocation) throws IOException
	{
		try(final Stream<Path> stream = Files.list(backupLocation))
		{
			final List<Path> backups = stream
					.sorted(Comparator.comparing(this::getLastModified))
					.collect(Collectors.toList());

			if(backups.size() < backupConfigurationProperties.getKeep())
			{
				return;
			}

			final List<Path> removalBackups = backups.subList(0, backups.size() - backupConfigurationProperties.getKeep());
			for(Path removalBackup : removalBackups)
			{
				LOGGER.info("Remove backup: {}", removalBackup);
				FileUtils.deleteDirectory(removalBackup.toFile());
			}
		}
	}

	private FileTime getLastModified(Path item)
	{
		try
		{
			return Files.getLastModifiedTime(item);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
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
