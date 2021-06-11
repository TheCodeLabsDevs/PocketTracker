package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BackupAddedShowModel
{
	@JsonProperty("show")
	private Integer showId;

	@JsonProperty("disliked")
	private Boolean disliked;

	public Integer getShowId()
	{
		return showId;
	}

	public void setShowId(Integer showId)
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
