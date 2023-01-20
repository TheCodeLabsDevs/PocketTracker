package de.thecodelabs.pockettracker.backup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thecodelabs.pockettracker.administration.apiconfiguration.APIConfigurationRepository;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfiguration;
import de.thecodelabs.pockettracker.backup.configuration.BackupConfigurationProperties;
import de.thecodelabs.pockettracker.backup.converter.APIConfigurationConverter;
import de.thecodelabs.pockettracker.backup.converter.ShowConverter;
import de.thecodelabs.pockettracker.backup.converter.user.UserConverter;
import de.thecodelabs.pockettracker.backup.model.BackupAPIConfigurationModel;
import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.Database;
import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static de.thecodelabs.pockettracker.backup.service.BackupService.DATABASE_PATH_NAME;
import static de.thecodelabs.pockettracker.backup.service.BackupService.IMAGE_PATH_NAME;

@Component
public class BackupCreateService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupCreateService.class);

	private final ShowRepository showRepository;
	private final UserRepository userRepository;
	private final APIConfigurationRepository apiConfigurationRepository;

	private final ShowConverter showConverter;
	private final UserConverter userConverter;
	private final APIConfigurationConverter apiConfigurationConverter;

	private final WebConfigurationProperties webConfigurationProperties;
	private final BackupConfigurationProperties backupConfigurationProperties;

	private final ObjectMapper objectMapper;

	@Autowired
	public BackupCreateService(ShowRepository showRepository, UserRepository userRepository, APIConfigurationRepository apiConfigurationRepository, ShowConverter showConverter,
							   UserConverter userConverter, APIConfigurationConverter apiConfigurationConverter, WebConfigurationProperties webConfigurationProperties,
							   BackupConfigurationProperties backupConfigurationProperties, ObjectMapper objectMapper)
	{
		this.showRepository = showRepository;
		this.userRepository = userRepository;
		this.apiConfigurationRepository = apiConfigurationRepository;
		this.showConverter = showConverter;
		this.userConverter = userConverter;
		this.apiConfigurationConverter = apiConfigurationConverter;
		this.webConfigurationProperties = webConfigurationProperties;
		this.backupConfigurationProperties = backupConfigurationProperties;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public void export(Path backupLocationPath) throws IOException
	{
		LOGGER.info("Start backup...");

		final List<Show> shows = showRepository.findAll();
		final List<BackupShowModel> backupShowModels = showConverter.toBeans(shows);

		final List<User> users = userRepository.findAll();
		final List<BackupUserModel> backupUserModels = userConverter.toBeans(users);

		final List<APIConfiguration> apiConfigurations = apiConfigurationRepository.findAll();
		final List<BackupAPIConfigurationModel> backupAPIConfigurationModels = apiConfigurationConverter.toBeans(apiConfigurations);

		final Database database = new Database(backupShowModels, backupUserModels, backupAPIConfigurationModels);

		LOGGER.info("Create backup at {}", backupLocationPath);

		exportDatabase(backupLocationPath, database);
		if(Boolean.TRUE.equals(backupConfigurationProperties.getIncludeImages()))
		{
			LOGGER.info("Exporting images...");
			exportImages(backupLocationPath);
		}

		LOGGER.info("Backup done");
	}

	private void exportImages(Path basePath) throws IOException
	{
		final Path imagesSourcePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());
		final Path imageBackupPath = basePath.resolve(IMAGE_PATH_NAME);

		FileUtils.copyDirectory(imagesSourcePath.toFile(), imageBackupPath.toFile());
	}

	private void exportDatabase(Path basePath, Database database) throws IOException
	{
		final Path databasePath = basePath.resolve(DATABASE_PATH_NAME);
		final BufferedWriter writer = Files.newBufferedWriter(databasePath);

		objectMapper.writer().writeValue(writer, database);
	}
}
