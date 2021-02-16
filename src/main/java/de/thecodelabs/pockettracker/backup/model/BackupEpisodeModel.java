package de.thecodelabs.pockettracker.backup.model;

import java.time.LocalDate;

public class BackupEpisodeModel
{
	private Integer id;
	private String name;
	private String description;
	private Integer number;
	private LocalDate firstAired;
	private Integer lengthInMinutes;
	private String posterPath;

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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}

	public LocalDate getFirstAired()
	{
		return firstAired;
	}

	public void setFirstAired(LocalDate firstAired)
	{
		this.firstAired = firstAired;
	}

	public Integer getLengthInMinutes()
	{
		return lengthInMinutes;
	}

	public void setLengthInMinutes(Integer lengthInMinutes)
	{
		this.lengthInMinutes = lengthInMinutes;
	}

	public String getPosterPath()
	{
		return posterPath;
	}

	public void setPosterPath(String posterPath)
	{
		this.posterPath = posterPath;
	}
}
