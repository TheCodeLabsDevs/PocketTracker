package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIConfigurationType;

public class BackupAPIConfigurationModel
{
	private Integer id;
	private APIConfigurationType type;
	private String token;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public APIConfigurationType getType()
	{
		return type;
	}

	public void setType(APIConfigurationType type)
	{
		this.type = type;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
}
