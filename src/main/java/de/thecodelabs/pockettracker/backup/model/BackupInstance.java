package de.thecodelabs.pockettracker.backup.model;

import org.apache.commons.io.FileUtils;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record BackupInstance(Path backupPath, long size, LocalDateTime createTime, boolean database, boolean images)
{
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
