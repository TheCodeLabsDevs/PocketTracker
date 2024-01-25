package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thecodelabs.pockettracker.show.model.ShowType;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BackupShowModel
{
	private UUID id;
	private String name;
	@JsonProperty("desc")
	private String description;
	@JsonProperty("first")
	private LocalDate firstAired;
	@JsonProperty("banner")
	private String bannerPath;
	@JsonProperty("poster")
	private String posterPath;
	private ShowType type;
	private Boolean finished;
	@MergeIgnore
	private List<BackupSeasonModel> seasons;
	@MergeIgnore
	private List<BackupAPIIdentifierModel> apiIdentifiers;

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

	public List<BackupSeasonModel> getSeasons()
	{
		return seasons;
	}

	public void setSeasons(List<BackupSeasonModel> seasons)
	{
		this.seasons = seasons;
	}

	public List<BackupAPIIdentifierModel> getApiIdentifiers()
	{
		return apiIdentifiers;
	}

	public void setApiIdentifiers(List<BackupAPIIdentifierModel> apiIdentifiers)
	{
		this.apiIdentifiers = apiIdentifiers;
	}
}
