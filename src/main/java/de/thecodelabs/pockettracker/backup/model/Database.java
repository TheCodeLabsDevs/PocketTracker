package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.backup.model.user.BackupUserModel;

import java.util.List;

public record Database(List<BackupShowModel> shows, List<BackupMovieModel> movies, List<BackupUserModel> users, List<BackupAPIConfigurationModel> apiConfigurations)
{
}
