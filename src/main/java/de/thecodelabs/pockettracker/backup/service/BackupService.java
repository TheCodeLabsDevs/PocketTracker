package de.thecodelabs.pockettracker.backup.service;

import de.thecodelabs.pockettracker.backup.configuration.BackupConfigurationProperties;
import de.thecodelabs.pockettracker.backup.model.BackupInstance;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class BackupService
{
	public static final String DATABASE_PATH_NAME = "database.json";
	public static final String IMAGE_PATH_NAME = "images";

	private static final Logger LOGGER = LoggerFactory.getLogger(BackupService.class);

	private final BackupCreateService backupCreateService;
	private final BackupRestoreService backupRestoreService;
	private final BackupConfigurationProperties backupConfigurationProperties;

	@Autowired
	public BackupService(BackupCreateService backupCreateService, BackupRestoreService backupRestoreService,
						 BackupConfigurationProperties backupConfigurationProperties)
	{
		this.backupCreateService = backupCreateService;
		this.backupRestoreService = backupRestoreService;
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

	public void clearData() throws SQLException
	{
		backupRestoreService.clearDatabase();
	}

	public void restoreBackup(Path zipPath) throws IOException, SQLException
	{
		final Path restoreBasePath = extractBackup(zipPath);

		clearData();
		backupRestoreService.insertAllData(restoreBasePath);
		backupRestoreService.copyImages(restoreBasePath);

		Files.delete(zipPath);
		FileUtils.deleteDirectory(restoreBasePath.toFile());

		LOGGER.info("Backup restore completed");
	}

	public List<BackupInstance> getBackups() throws IOException
	{
		try(final Stream<Path> stream = Files.list(Paths.get(backupConfigurationProperties.getLocation())))
		{
			return stream
					.filter(Files::isDirectory)
					.map(path -> {
						final long size = FileUtils.sizeOfDirectory(path.toFile());
						final LocalDateTime createTime = LocalDateTime.ofInstant(getLastModified(path).toInstant(), ZoneId.systemDefault());
						final boolean includeDatabase = Files.exists(path.resolve(DATABASE_PATH_NAME));
						final boolean includeImages = Files.exists(path.resolve(IMAGE_PATH_NAME));

						return new BackupInstance(path, size, createTime, includeDatabase, includeImages);
					})
					.sorted(Comparator.comparing(BackupInstance::getCreateTime).reversed())
					.collect(Collectors.toList());
		}
	}

	public byte[] getBackupBundled(String path) throws IOException
	{
		final Path backupPath = Paths.get(backupConfigurationProperties.getLocation()).resolve(path);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

		try(final Stream<Path> walk = Files.walk(backupPath))
		{
			walk.filter(Files::isRegularFile)
					.forEach(child -> {
						try
						{
							final String pathInZip = backupPath.relativize(child).toString();
							zipOutputStream.putNextEntry(new ZipEntry(pathInZip));
							IOUtils.copy(Files.newInputStream(child), zipOutputStream);
						}
						catch(IOException e)
						{
							LOGGER.error("Error copy file into zip", e);
						}
					});
		}

		zipOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}

	public Path extractBackup(Path zipPath) throws IOException
	{
		final Path restorePath = Paths.get(backupConfigurationProperties.getLocation(), "restore");
		if(Files.notExists(restorePath))
		{
			Files.createDirectories(restorePath);
		}

		try(final InputStream inputStream = Files.newInputStream(zipPath);
			final ZipInputStream zipInputStream = new ZipInputStream(inputStream))
		{
			ZipEntry entry;
			while((entry = zipInputStream.getNextEntry()) != null)
			{
				LOGGER.info("Extract: {}", entry.getName());
				final Path targetChildPath = restorePath.resolve(entry.getName());
				if(!entry.isDirectory())
				{
					if(Files.notExists(targetChildPath.getParent()))
					{
						Files.createDirectories(targetChildPath.getParent());
					}
					if(Files.notExists(targetChildPath))
					{
						Files.createFile(targetChildPath);
					}

					try(final OutputStream outputStream = Files.newOutputStream(targetChildPath))
					{
						IOUtils.copy(zipInputStream, outputStream);
					}
				}
			}
		}

		return restorePath;
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
