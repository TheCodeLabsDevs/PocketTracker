package de.thecodelabs.pockettracker.backup.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thecodelabs.pockettracker.backup.model.BackupAddedMovieModel;
import de.thecodelabs.pockettracker.backup.model.BackupHiddenShowModel;
import de.thecodelabs.pockettracker.backup.model.BackupWatchedEpisodeModel;
import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BackupUserModel
{
	private UUID id;

	private String name;

	private List<BackupUserAuthentication> authentications;

	private List<BackupUserTokenModel> tokens;

	private UserRole userRole;

	@MergeIgnore
	private BackupUserSettingsModel settings;

	@MergeIgnore
	private List<BackupHiddenShowModel> hiddenShows;

	@MergeIgnore
	private List<BackupAddedMovieModel> movies;

	@JsonProperty("watched")
	private List<BackupWatchedEpisodeModel> watchedEpisodes;
}
