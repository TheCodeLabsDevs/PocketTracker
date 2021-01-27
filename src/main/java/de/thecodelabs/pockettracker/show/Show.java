package de.thecodelabs.pockettracker.show;


import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.season.Season;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Show
{
	public static class View
	{
		interface Summery
		{
		}
	}

	@Id
	@GeneratedValue
	@JsonView(View.Summery.class)
	private Integer id;

	@NotNull
	@JsonView(View.Summery.class)
	private String name;

	@Column(length = 4096)
	@JsonView(View.Summery.class)
	private String description;

	@JsonView(View.Summery.class)
	private LocalDate firstAired;

	@Column(length = 2048)
	@JsonView(View.Summery.class)
	private String bannerPath;

	@Column(length = 2048)
	@JsonView(View.Summery.class)
	private String posterPath;

	@JsonView(View.Summery.class)
	private ShowType type;

	@OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
	private List<Season> seasons = new ArrayList<>();

	public Show()
	{
	}

	public Show(@NotNull String name, String description, LocalDate firstAired, String bannerPath, String posterPath, ShowType type)
	{
		this.name = name;
		this.description = description;
		this.firstAired = firstAired;
		this.bannerPath = bannerPath;
		this.posterPath = posterPath;
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

	public String getBannerPath()
	{
		return bannerPath;
	}

	public void setBannerPath(String imagePath)
	{
		this.bannerPath = imagePath;
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
				", bannerPath='" + bannerPath + '\'' +
				", posterPath='" + posterPath + '\'' +
				", type=" + type +
				", seasons=" + seasons +
				'}';
	}
}
