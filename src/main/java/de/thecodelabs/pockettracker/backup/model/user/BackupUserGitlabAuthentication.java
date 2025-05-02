package de.thecodelabs.pockettracker.backup.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackupUserGitlabAuthentication extends BackupUserAuthentication
{
	private String gitlabUsername;
}
