package de.thecodelabs.pockettracker.user.model;


import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "appuser_settings")
public class UserSettings
{
	@Id
	@GeneratedValue(generator = "custom_generator")
	@GenericGenerator(name = "custom_generator", strategy = "de.thecodelabs.pockettracker.utils.CustomIdGenerator")
	private Integer id;

	private Boolean showDislikedShows = false;
	private ShowSortOption lastShowSortOption = ShowSortOption.LAST_WATCHED;
	private ShowFilterOption lastShowFilterOption = ShowFilterOption.ALL_SHOWS;

	@OneToOne(mappedBy = "settings")
	private User user;

	public UserSettings()
	{
	}

	public UserSettings(User user)
	{
		this.user = user;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
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
