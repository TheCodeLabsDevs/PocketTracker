package de.thecodelabs.pockettracker.backup.model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = BackupUserInternalAuthentication.class, name = "internal"),
		@JsonSubTypes.Type(value = BackupUserGitlabAuthentication.class, name = "gitlab")
})

public abstract class BackupUserAuthentication
{
	private UUID id;

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}
}
