package de.thecodelabs.pockettracker.backup.model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = BackupUserInternalAuthentication.class, name = "internal"),
		@JsonSubTypes.Type(value = BackupUserGitlabAuthentication.class, name = "gitlab")
})
@Getter
@Setter
public abstract class BackupUserAuthentication
{
	private UUID id;
}
