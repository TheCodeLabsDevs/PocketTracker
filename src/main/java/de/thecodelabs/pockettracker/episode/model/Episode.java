package de.thecodelabs.pockettracker.episode.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import de.thecodelabs.pockettracker.utils.json.JsonResourcePathSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Episode
{
	@Id
	private UUID id;

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

	@Column(length = 2048)
	@JsonSerialize(using = JsonResourcePathSerializer.class)
	private String posterPath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Season season;

	@OneToMany(mappedBy = "episode")
	@JsonIgnore
	@MergeIgnore
	private List<WatchedEpisode> watchedEpisodes;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

	public Episode()
	{
	}

	public Episode(String name, Integer number, Season season)
	{
		this.name = name;
		this.number = number;
		this.season = season;
	}

	public Episode(String name, String description, Integer number, LocalDate firstAired, Integer lengthInMinutes, Season season, String posterPath)
	{
		this.name = name;
		this.description = description;
		this.number = number;
		this.firstAired = firstAired;
		this.lengthInMinutes = lengthInMinutes;
		this.season = season;
		this.posterPath = posterPath;
	}

	public String getFirstAiredReadable()
	{
		if(firstAired == null)
		{
			return null;
		}
		return firstAired.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public String getImagePath(EpisodeImageType episodeImageType)
	{
		if(episodeImageType == EpisodeImageType.POSTER)
		{
			return getPosterPath();
		}
		throw new UnsupportedOperationException("Image type not implemented");
	}

	public void setImagePath(EpisodeImageType episodeImageType, String path)
	{
		if(episodeImageType == EpisodeImageType.POSTER)
		{
			setPosterPath(path);
			return;
		}
		throw new UnsupportedOperationException("Image type not implemented");
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Episode episode = (Episode) o;
		return Objects.equals(id, episode.id)
				&& Objects.equals(name, episode.name)
				&& Objects.equals(description, episode.description)
				&& Objects.equals(number, episode.number)
				&& Objects.equals(firstAired, episode.firstAired)
				&& Objects.equals(lengthInMinutes, episode.lengthInMinutes)
				&& Objects.equals(posterPath, episode.posterPath);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, description, number, firstAired, lengthInMinutes, posterPath);
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
				", posterPath=" + posterPath +
				", season=[id=" + season.getId() + ", name=" + season.getName() + ", number: " + season.getNumber() + "]" +
				'}';
	}
}
