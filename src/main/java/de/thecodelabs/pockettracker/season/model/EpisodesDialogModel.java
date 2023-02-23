package de.thecodelabs.pockettracker.season.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EpisodesDialogModel
{
	@NotNull
	@Min(0)
	private Integer episodeCount;

	public Integer getEpisodeCount()
	{
		return episodeCount;
	}

	public void setEpisodeCount(Integer episodeCount)
	{
		this.episodeCount = episodeCount;
	}
}