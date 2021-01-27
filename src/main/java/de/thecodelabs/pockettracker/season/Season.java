package de.thecodelabs.pockettracker.season;


import com.fasterxml.jackson.annotation.JsonIgnore;
import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.show.Show;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Season
{
	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@Column(length = 2048)
	private String description;

	private Integer number;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Show show;

	@OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
	private List<Episode> episodes = new ArrayList<>();

	public Season()
	{
	}

	public Season(String name, String description, Integer number, Show show)
	{
		this.name = name;
		this.description = description;
		this.number = number;
		this.show = show;
	}

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
				'}';
	}
}
