package de.thecodelabs.pockettracker.show.model;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.thecodelabs.pockettracker.season.model.Season;
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
		public interface Summary
		{
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(View.Summary.class)
	private Integer id;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@JsonView(View.Summary.class)
	private String name = "";

	@Column(length = 4096)
	@Size(max = 4096)
	@JsonView(View.Summary.class)
	private String description;

	@JsonView(View.Summary.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate firstAired;

	@Column(length = 2048)
	@JsonView(View.Summary.class)
	@JsonSerialize(using = JsonResourcePathSerializer.class)
	private String bannerPath;

	@Column(length = 2048)
	@JsonView(View.Summary.class)
	@JsonSerialize(using = JsonResourcePathSerializer.class)
	private String posterPath;

	@JsonView(View.Summary.class)
	@NotNull
	private ShowType type;

	@JsonView(View.Summary.class)
	private Boolean finished = false;


	@OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
	@MergeIgnore
	private List<Season> seasons = new ArrayList<>();

	public Show()
	{
	}

	public Show(@NotNull String name, String description, LocalDate firstAired, String bannerPath, String posterPath, ShowType type, boolean finished)
	{
		this.name = name;
		this.description = description;
		this.firstAired = firstAired;
		this.bannerPath = bannerPath;
		this.posterPath = posterPath;
		this.type = type;
		this.finished = finished;
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

	public Boolean getFinished()
	{
		return finished;
	}

	public void setFinished(Boolean finished)
	{
		this.finished = finished;
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

	public String getImagePath(ShowImageType showImageType) {
		switch (showImageType) {
			case BANNER:
				return getBannerPath();
			case POSTER:
				return getPosterPath();
			default:
				throw new UnsupportedOperationException("Image type not implemented");
		}
	}

	public void setImagePath(ShowImageType showImageType, String path) {
		switch (showImageType) {
			case BANNER:
				setBannerPath(path);
				break;
			case POSTER:
				setPosterPath(path);
				break;
			default:
				throw new UnsupportedOperationException("Image type not implemented");
		}
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
				", finished=" + finished +
				", seasons=" + seasons +
				'}';
	}
}