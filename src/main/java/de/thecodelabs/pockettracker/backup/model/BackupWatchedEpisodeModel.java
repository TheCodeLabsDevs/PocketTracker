package de.thecodelabs.pockettracker.backup.model;

import java.time.LocalDate;

public class BackupWatchedEpisodeModel
{
	private Integer episodeId;
	private LocalDate watchedAt;

	public Integer getEpisodeId()
	{
		return episodeId;
	}

	public void setEpisodeId(Integer episodeId)
	{
		this.episodeId = episodeId;
	}

	public LocalDate getWatchedAt()
	{
		return watchedAt;
	}

	public void setWatchedAt(LocalDate watchedAt)
	{
		this.watchedAt = watchedAt;
	}
}
