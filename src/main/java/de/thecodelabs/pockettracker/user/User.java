package de.thecodelabs.pockettracker.user;

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
	private String name;

	private String password;

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

	@Override
	public String toString()
	{
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}