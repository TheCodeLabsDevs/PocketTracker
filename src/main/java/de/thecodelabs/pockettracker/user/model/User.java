package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.show.Show;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "appuser")
public class User
{
	@Id
	@GeneratedValue
	private Integer id;

	@NotNull
	@Column(unique = true)
	private String name;

	private String password;

	@NotNull
	private UserRole userRole;

	@ManyToMany
	private List<Show> shows;

	@ManyToMany
	private List<Episode> watchedEpisodes;

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public UserRole getUserRole()
	{
		return userRole;
	}

	public void setUserRole(UserRole userRole)
	{
		this.userRole = userRole;
	}

	public List<Show> getShows()
	{
		return shows;
	}

	public List<Episode> getWatchedEpisodes()
	{
		return watchedEpisodes;
	}

	@Override
	public String toString()
	{
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", userRole=" + userRole +
				'}';
	}
}
