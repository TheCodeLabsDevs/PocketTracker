package de.thecodelabs.pockettracker.season.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
public class Season
{
	public static class View
	{
		public interface Summary
		{
		}
	}

	@Id
	@JsonView(View.Summary.class)
	private UUID id;

	@NotNull
	@NotEmpty
	@JsonView(View.Summary.class)
	private String name;

	@Column(length = 4096)
	@JsonView(View.Summary.class)
	private String description;

	@NotNull
	@Min(0)
	@JsonView(View.Summary.class)
	private Integer number;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@MergeIgnore
	private Show show;

	@OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
	@MergeIgnore
	private List<Episode> episodes = new ArrayList<>();

	private Boolean filledCompletely = false;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

	public Season()
	{
	}

	public Season(String name, String description, Integer number)
	{
		this(name, description, number, null);
	}

	public Season(String name, String description, Integer number, Show show)
	{
		this.name = name;
		this.description = description;
		this.number = number;
		this.show = show;
		this.filledCompletely = false;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public UUID getId()
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

	public Show getShow()
	{
		return show;
	}

	public void setShow(Show show)
	{
		this.show = show;
	}

	public List<Episode> getEpisodes()
	{
		return episodes;
	}

	public void setEpisodes(List<Episode> episodes)
	{
		this.episodes = episodes;
	}

	public Boolean getFilledCompletely()
	{
		return filledCompletely;
	}

	public void setFilledCompletely(Boolean filledCompletely)
	{
		this.filledCompletely = filledCompletely;
	}

	public void addEpisode(Episode episode)
	{
		if(episodes == null)
		{
			episodes = new ArrayList<>();
		}
		episodes.add(episode);
	}

	public Optional<Episode> getEpisodeByNumber(int episodeNumber)
	{
		return episodes.stream()
				.filter(episode -> episode.getNumber().equals(episodeNumber))
				.findFirst();
	}

	@Override
	public String toString()
	{
		return "Season{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", number=" + number +
				", show=[id=" + show.getId() + ", name=" + show.getName() + "]" +
				", episodes=" + episodes +
				", filledCompletely=" + filledCompletely +
				'}';
	}
}
