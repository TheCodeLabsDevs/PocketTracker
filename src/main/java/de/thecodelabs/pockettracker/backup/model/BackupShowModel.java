package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.show.model.ShowType;

import java.time.LocalDate;

public class BackupShowModel
{
	private Integer id;
	private String name;
	private String description;
	private LocalDate firstAired;
	private String bannerPath;
	private String posterPath;
	private ShowType type;
	private Boolean finished;

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

	public LocalDate getFirstAired()
	{
		return firstAired;
	}

	public void setFirstAired(LocalDate firstAired)
	{
		this.firstAired = firstAired;
	}

	public String getBannerPath()
	{
		return bannerPath;
	}

	public void setBannerPath(String bannerPath)
	{
		this.bannerPath = bannerPath;
	}

	public String getPosterPath()
	{
		return posterPath;
	}

	public void setPosterPath(String posterPath)
	{
		this.posterPath = posterPath;
	}

	public ShowType getType()
	{
		return type;
	}

	public void setType(ShowType type)
	{
		this.type = type;
	}

	public Boolean getFinished()
	{
		return finished;
	}

	public void setFinished(Boolean finished)
	{
		this.finished = finished;
	}
}
