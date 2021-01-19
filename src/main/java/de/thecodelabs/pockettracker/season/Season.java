package de.thecodelabs.pockettracker.season;


import de.thecodelabs.pockettracker.show.Show;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Season
{
	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	private String description;

	private Integer number;

	@ManyToOne(fetch = FetchType.LAZY)
	private Show show;

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

	@Override
	public String toString()
	{
		return "Season{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", show=[id=" + show.getId() + ", name=" + show.getName() + "]" +
				", number=" + number +
				'}';
	}
}
