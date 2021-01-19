package de.thecodelabs.pockettracker.show;


import de.thecodelabs.pockettracker.season.Season;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Show
{
	@Id
	@GeneratedValue
	private Integer id;

	@NotNull
	private String name;

	private String description;

	private LocalDate firstAired;

	private String imagePath;

	private ShowType type;

	@OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
	private List<Season> seasons = new ArrayList<>();

	public Show()
	{
	}

	public Show(@NotNull String name, String description, LocalDate firstAired, String imagePath, ShowType type)
	{
		this.name = name;
		this.description = description;
		this.firstAired = firstAired;
		this.imagePath = imagePath;
		this.type = type;
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

	public LocalDate getFirstAired()
	{
		return firstAired;
	}

	public void setFirstAired(LocalDate firstAired)
	{
		this.firstAired = firstAired;
	}

	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	public ShowType getType()
	{
		return type;
	}

	public void setType(ShowType type)
	{
		this.type = type;
	}

	public List<Season> getSeasons()
	{
		return seasons;
	}

	public void setSeasons(List<Season> seasons)
	{
		this.seasons = seasons;
	}

	@Override
	public String toString()
	{
		return "Show{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", firstAired=" + firstAired +
				", imagePath='" + imagePath + '\'' +
				", type=" + type +
				", seasons=" + seasons +
				'}';
	}
}
