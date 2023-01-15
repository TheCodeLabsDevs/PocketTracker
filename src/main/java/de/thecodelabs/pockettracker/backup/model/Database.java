package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;

import java.util.List;

public record Database(List<BackupShowModel> shows, List<BackupUserModel> users)
{
}
