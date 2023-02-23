package de.thecodelabs.pockettracker.show.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SeasonsDialogModel
{
	@NotNull
	@Min(0)
	private Integer seasonCount;

	public Integer getSeasonCount()
	{
		return seasonCount;
	}

	public void setSeasonCount(Integer seasonCount)
	{
		this.seasonCount = seasonCount;
	}
}
