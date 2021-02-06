package de.thecodelabs.pockettracker.show.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
