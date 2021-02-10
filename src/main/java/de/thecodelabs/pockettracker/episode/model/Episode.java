package de.thecodelabs.pockettracker.episode.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Entity
public class Episode
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String name;

	@Size(max = 4096)
	@Column(length = 4096)
	private String description;

	private Integer number;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate firstAired;

	@Min(0)
	private Integer lengthInMinutes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Season season;

	@OneToMany(mappedBy = "episode")
	@JsonIgnore
	@MergeIgnore
	private List<WatchedEpisode> watchedEpisodes;

	public Episode()
	{
	}

	public Episode(String name, Integer number, Season season)
	{
		this.name = name;
		this.number = number;
		this.season = season;
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

	public String getFirstAiredReadable()
	{
		if(firstAired == null)
		{
			return null;
		}
		return firstAired.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public List<WatchedEpisode> getWatchedEpisodes()
	{
		return watchedEpisodes;
	}

	public void setWatchedEpisodes(List<WatchedEpisode> watchedEpisodes)
	{
		this.watchedEpisodes = watchedEpisodes;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Episode)) return false;
		Episode episode = (Episode) o;
		return Objects.equals(id, episode.id) && Objects.equals(name, episode.name)
				&& Objects.equals(description, episode.description) && Objects.equals(number, episode.number)
				&& Objects.equals(firstAired, episode.firstAired)
				&& Objects.equals(lengthInMinutes, episode.lengthInMinutes);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, description, number, firstAired, lengthInMinutes);
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
