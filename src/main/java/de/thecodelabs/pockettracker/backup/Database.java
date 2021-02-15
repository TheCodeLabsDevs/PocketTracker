package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.backup.model.BackupShowModel;
import de.thecodelabs.pockettracker.backup.model.BackupUserModel;

import java.util.List;

public class Database
{
	private final List<BackupShowModel> shows;
	private final List<BackupUserModel> users;

	public Database(List<BackupShowModel> shows, List<BackupUserModel> users)
	{
		this.shows = shows;
		this.users = users;
	}

	public List<BackupShowModel> getShows()
	{
		return shows;
	}

	public List<BackupUserModel> getUsers()
	{
		return users;
	}
}
