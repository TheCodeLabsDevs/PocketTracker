package de.thecodelabs.pockettracker.episode;


import com.fasterxml.jackson.annotation.JsonIgnore;
import de.thecodelabs.pockettracker.season.Season;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Episode
{
	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@Column(length = 2048)
	private String description;

	private Integer number;

	private LocalDate firstAired;

	private Integer lengthInMinutes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Season season;

	public Episode()
	{
	}

	public Episode(String name, String description, Integer number, LocalDate firstAired, Integer lengthInMinutes, Season season)
	{
		this.name = name;
		this.description = description;
		this.number = number;
		this.firstAired = firstAired;
		this.lengthInMinutes = lengthInMinutes;
		this.season = season;
	}

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

	public Season getSeason()
	{
		return season;
	}

	public void setSeason(Season season)
	{
		this.season = season;
	}

	@Override
	public String toString()
	{
		return "Episode{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", number=" + number +
				", firstAired=" + firstAired +
				", lengthInMinutes=" + lengthInMinutes +
				", season=[id=" + season.getId() + ", name=" + season.getName() + ", number: " + season.getNumber() + "]" +
				'}';
	}
}
