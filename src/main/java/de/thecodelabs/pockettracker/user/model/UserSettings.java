package de.thecodelabs.pockettracker.user.model;


import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "appuser_settings")
public class UserSettings
{
	@Id
	private UUID id;

	private Boolean showDislikedShows = false;
	private ShowSortOption lastShowSortOption = ShowSortOption.LAST_WATCHED;
	private ShowFilterOption lastShowFilterOption = ShowFilterOption.ALL_SHOWS;

	@OneToOne(mappedBy = "settings")
	private User user;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

	public UserSettings()
	{
	}

	public UserSettings(User user)
	{
		this.user = user;
	}

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public ShowSortOption getLastShowSortOption()
	{
		return lastShowSortOption;
	}

	public void setLastShowSortOption(ShowSortOption lastShowSortOption)
	{
		this.lastShowSortOption = lastShowSortOption;
	}

	public ShowFilterOption getLastShowFilterOption()
	{
		return lastShowFilterOption;
	}

	public void setLastShowFilterOption(ShowFilterOption lastShowFilterOption)
	{
		this.lastShowFilterOption = lastShowFilterOption;
	}

	public Boolean getShowDislikedShows()
	{
		return showDislikedShows;
	}

	public void setShowDislikedShows(Boolean showDislikedShows)
	{
		this.showDislikedShows = showDislikedShows;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
}
