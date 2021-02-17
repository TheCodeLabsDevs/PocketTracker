package de.thecodelabs.pockettracker.backup.model.user;

public class BackupUserGitlabAuthentication extends BackupUserAuthentication
{
	private String gitlabUsername;

	public String getGitlabUsername()
	{
		return gitlabUsername;
	}

	public void setGitlabUsername(String gitlabUsername)
	{
		this.gitlabUsername = gitlabUsername;
	}
}
