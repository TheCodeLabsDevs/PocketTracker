package de.thecodelabs.pockettracker.backup.model.user;

public class BackupUserInternalAuthentication extends BackupUserAuthentication
{
	private String password;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
