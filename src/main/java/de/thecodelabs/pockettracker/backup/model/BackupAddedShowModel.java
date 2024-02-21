package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class BackupAddedShowModel
{
	@JsonProperty("show")
	private UUID showId;

	@JsonProperty("disliked")
	private Boolean disliked;

	public UUID getShowId()
	{
		return showId;
	}

	public void setShowId(UUID showId)
	{
		this.showId = showId;
	}

	public Boolean getDisliked()
	{
		return disliked;
	}

	public void setDisliked(Boolean disliked)
	{
		this.disliked = disliked;
	}
}
