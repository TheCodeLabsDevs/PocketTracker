package de.thecodelabs.pockettracker.backup.model;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupInstance
{
	private Path backupPath;
	private LocalDateTime createTime;
	private Boolean database;
	private Boolean images;

	public BackupInstance(Path backupPath, LocalDateTime createTime, Boolean database, Boolean images)
	{
		this.backupPath = backupPath;
		this.createTime = createTime;
		this.database = database;
		this.images = images;
	}

	public Path getBackupPath()
	{
		return backupPath;
	}

	public void setBackupPath(Path backupPath)
	{
		this.backupPath = backupPath;
	}

	public LocalDateTime getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime)
	{
		this.createTime = createTime;
	}

	public Boolean getDatabase()
	{
		return database;
	}

	public void setDatabase(Boolean database)
	{
		this.database = database;
	}

	public Boolean getImages()
	{
		return images;
	}

	public void setImages(Boolean images)
	{
		this.images = images;
	}

	public String getName()
	{
		return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"));
	}

	public String getPathName()
	{
		return URLEncoder.encode(backupPath.getFileName().toString(), Charset.defaultCharset());
	}
}
