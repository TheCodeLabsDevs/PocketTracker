package de.thecodelabs.pockettracker.backup.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thecodelabs.pockettracker.backup.model.BackupAddedMovieModel;
import de.thecodelabs.pockettracker.backup.model.BackupAddedShowModel;
import de.thecodelabs.pockettracker.backup.model.BackupWatchedEpisodeModel;
import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;

import java.util.List;
import java.util.UUID;

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
	private List<BackupAddedShowModel> shows;
	@MergeIgnore
	private List<BackupAddedMovieModel> movies;
	@JsonProperty("watched")
	private List<BackupWatchedEpisodeModel> watchedEpisodes;

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<BackupUserAuthentication> getAuthentications()
	{
		return authentications;
	}

	public void setAuthentications(List<BackupUserAuthentication> authentications)
	{
		this.authentications = authentications;
	}

	public List<BackupUserTokenModel> getTokens()
	{
		return tokens;
	}

	public void setTokens(List<BackupUserTokenModel> tokens)
	{
		this.tokens = tokens;
	}

	public UserRole getUserRole()
	{
		return userRole;
	}

	public void setUserRole(UserRole userRole)
	{
		this.userRole = userRole;
	}

	public BackupUserSettingsModel getSettings()
	{
		return settings;
	}

	public void setSettings(BackupUserSettingsModel settings)
	{
		this.settings = settings;
	}

	public List<BackupAddedShowModel> getShows()
	{
		return shows;
	}

	public void setShows(List<BackupAddedShowModel> shows)
	{
		this.shows = shows;
	}

	public List<BackupAddedMovieModel> getMovies()
	{
		return movies;
	}

	public void setMovies(List<BackupAddedMovieModel> movies)
	{
		this.movies = movies;
	}

	public List<BackupWatchedEpisodeModel> getWatchedEpisodes()
	{
		return watchedEpisodes;
	}

	public void setWatchedEpisodes(List<BackupWatchedEpisodeModel> watchedEpisodes)
	{
		this.watchedEpisodes = watchedEpisodes;
	}
}
