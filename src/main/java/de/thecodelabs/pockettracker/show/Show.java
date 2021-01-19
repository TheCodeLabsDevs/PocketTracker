package de.thecodelabs.pockettracker.show;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Show
{
	@Id
	private Integer id;

	@NotNull
	private String name;

	private String description;

	private LocalDate firstAired;

	private String imagePath;

	private ShowType type;


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

	@Override
	public String toString()
	{
		return "Show{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", firstAired=" + firstAired +
				", imagePath='" + imagePath + '\'' +
				", type=" + type +
				'}';
	}
}
