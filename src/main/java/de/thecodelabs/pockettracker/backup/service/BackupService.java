package de.thecodelabs.pockettracker.backup.service;

import de.thecodelabs.pockettracker.backup.configuration.BackupConfigurationProperties;
import de.thecodelabs.pockettracker.backup.model.BackupInstance;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BackupService
{
	public static final String DATABASE_PATH_NAME = "database.json";
	public static final String IMAGE_PATH_NAME = "images";

	private static final Logger LOGGER = LoggerFactory.getLogger(BackupService.class);

	private final BackupCreateService backupCreateService;
	private final BackupConfigurationProperties backupConfigurationProperties;

	@Autowired
	public BackupService(BackupCreateService backupCreateService, BackupConfigurationProperties backupConfigurationProperties)
	{
		this.backupCreateService = backupCreateService;
		this.backupConfigurationProperties = backupConfigurationProperties;
	}

	public void createBackup() throws IOException
	{
		final String backupFolderName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
		final Path basePath = Paths.get(backupConfigurationProperties.getLocation());

		final Path backupLocationPath = basePath.resolve(backupFolderName);
		if(Files.notExists(backupLocationPath))
		{
			Files.createDirectories(backupLocationPath);
		}

		LOGGER.info("Cleaning old backups...");
		deleteOldBackups(basePath);

		backupCreateService.export(backupLocationPath);
	}

	public List<BackupInstance> getBackups() throws IOException
	{
		try(final Stream<Path> stream = Files.list(Paths.get(backupConfigurationProperties.getLocation())))
		{
			return stream.map(path -> {
				final LocalDateTime createTime = LocalDateTime.ofInstant(getLastModified(path).toInstant(), ZoneId.systemDefault());
				final boolean includeDatabase = Files.exists(path.resolve(DATABASE_PATH_NAME));
				final boolean includeImages = Files.exists(path.resolve(IMAGE_PATH_NAME));

				return new BackupInstance(path, createTime, includeDatabase, includeImages);
			})
					.sorted(Comparator.comparing(BackupInstance::getCreateTime).reversed())
					.collect(Collectors.toList());
		}
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
}
