package de.thecodelabs.pockettracker.backup.model.user;

import de.thecodelabs.pockettracker.show.model.ShowSortOption;

public class BackupUserSettingsModel
{
	private ShowSortOption lastShowSortOption;

	public ShowSortOption getLastShowSortOption()
	{
		return lastShowSortOption;
	}

	public void setLastShowSortOption(ShowSortOption lastShowSortOption)
	{
		this.lastShowSortOption = lastShowSortOption;
	}
}
