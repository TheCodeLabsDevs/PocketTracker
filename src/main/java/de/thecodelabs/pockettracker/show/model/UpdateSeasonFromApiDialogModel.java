package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

public class UpdateSeasonFromApiDialogModel
{
	private APIType apiType;

	public APIType getApiType()
	{
		return apiType;
	}

	public void setApiType(APIType apiType)
	{
		this.apiType = apiType;
	}
}
