package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;

import java.util.List;

public class BackupUserModel
{
	private Integer id;
	private String name;
	// TODO: Authentication
	// TODO: Tokens
	private UserRole userRole;
	@MergeIgnore
	private List<Integer> shows;
	// TODO: watchedEpisodes


	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
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

	public UserRole getUserRole()
	{
		return userRole;
	}

	public void setUserRole(UserRole userRole)
	{
		this.userRole = userRole;
	}

	public List<Integer> getShows()
	{
		return shows;
	}

	public void setShows(List<Integer> shows)
	{
		this.shows = shows;
	}
}
