package de.thecodelabs.pockettracker.show;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.thecodelabs.pockettracker.season.Season;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import de.thecodelabs.pockettracker.utils.json.JsonResourcePathSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Show
{
	public static class View
	{
		public interface Summery
		{
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(View.Summery.class)
	private Integer id;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@JsonView(View.Summery.class)
	private String name = "";

	@Column(length = 4096)
	@Size(max = 4096)
	@JsonView(View.Summery.class)
	private String description;

	@JsonView(View.Summery.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate firstAired;

	@Column(length = 2048)
	@JsonView(View.Summery.class)
	@JsonSerialize(using = JsonResourcePathSerializer.class)
	private String bannerPath;

	@Column(length = 2048)
	@JsonView(View.Summery.class)
	@JsonSerialize(using = JsonResourcePathSerializer.class)
	private String posterPath;

	@JsonView(View.Summery.class)
	@NotNull
	private ShowType type;

	@OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
	@MergeIgnore
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

	public String getFirstAiredReadable()
	{
		if(firstAired == null)
		{
			return null;
		}
		return firstAired.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Show)) return false;
		Show show = (Show) o;
		return Objects.equals(id, show.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
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
