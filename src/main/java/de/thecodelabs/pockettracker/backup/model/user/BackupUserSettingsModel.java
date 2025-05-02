package de.thecodelabs.pockettracker.backup.model.user;

import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackupUserSettingsModel
{
	private ShowSortOption lastShowSortOption;
}
