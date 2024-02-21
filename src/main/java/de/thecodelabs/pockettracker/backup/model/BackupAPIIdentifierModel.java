package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

import java.util.UUID;

public class BackupAPIIdentifierModel
{
	private UUID id;
	private APIType type;
	private String identifier;

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
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
