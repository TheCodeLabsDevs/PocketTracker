package de.thecodelabs.pockettracker.backup.model;

import org.apache.commons.io.FileUtils;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupInstance
{
	private final Path backupPath;
	private final long size;
	private final LocalDateTime createTime;
	private final boolean database;
	private final boolean images;

	public BackupInstance(Path backupPath, long size, LocalDateTime createTime, boolean database, boolean images)
	{
		this.backupPath = backupPath;
		this.size = size;
		this.createTime = createTime;
		this.database = database;
		this.images = images;
	}

	public Path getBackupPath()
	{
		return backupPath;
	}

	public long getSize()
	{
		return size;
	}

	public LocalDateTime getCreateTime()
	{
		return createTime;
	}

	public boolean isDatabase()
	{
		return database;
	}

	public boolean isImages()
	{
		return images;
	}

	public String getName()
	{
		return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
	}

	public String getPathName()
	{
		return URLEncoder.encode(backupPath.getFileName().toString(), Charset.defaultCharset());
	}

	public String getSizeFormatted()
	{
		return FileUtils.byteCountToDisplaySize(size);
	}
}
