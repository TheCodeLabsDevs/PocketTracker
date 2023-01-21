package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

public class BackupAPIIdentifierModel
{
	private Integer id;
	private APIType type;
	private String identifier;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public APIType getType()
	{
		return type;
	}

	public void setType(APIType type)
	{
		this.type = type;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
}
