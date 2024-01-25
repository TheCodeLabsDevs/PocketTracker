package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public class BackupWatchedEpisodeModel
{
	@JsonProperty("episode")
	private UUID episodeId;
	@JsonProperty("at")
	private LocalDate watchedAt;

	public UUID getEpisodeId()
	{
		return episodeId;
	}

	public void setEpisodeId(UUID episodeId)
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
