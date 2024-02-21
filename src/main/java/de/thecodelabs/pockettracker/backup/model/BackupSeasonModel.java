package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;

import java.util.List;
import java.util.UUID;

public class BackupSeasonModel
{
	private UUID id;
	private String name;
	@JsonProperty("desc")
	private String description;
	private Integer number;
	@MergeIgnore
	private List<BackupEpisodeModel> episodes;
	private Boolean filledCompletely;

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
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

	public List<BackupEpisodeModel> getEpisodes()
	{
		return episodes;
	}

	public void setEpisodes(List<BackupEpisodeModel> episodes)
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
}
