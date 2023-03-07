package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

public class SeasonFromApiDialogModel
{
	private APIType apiType;
	private Integer seasonId;

	public APIType getApiType()
	{
		return apiType;
	}

	public void setApiType(APIType apiType)
	{
		this.apiType = apiType;
	}

	public Integer getSeasonId()
	{
		return seasonId;
	}

	public void setSeasonId(Integer seasonId)
	{
		this.seasonId = seasonId;
	}
}
