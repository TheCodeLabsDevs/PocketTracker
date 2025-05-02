package de.thecodelabs.pockettracker.show.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.mediaitem.MediaItem;
import de.thecodelabs.pockettracker.mediaitem.MediaItemImageType;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import de.thecodelabs.pockettracker.utils.json.JsonResourcePathSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
public class Show implements MediaItem
{
	public static class View
	{
		public interface Summary
		{
		}
	}

	@Id
	@JsonView(View.Summary.class)
	private UUID id;

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

	@OneToMany(mappedBy = "show")
	@MergeIgnore
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private List<AddedShow> favoriteUsers = new ArrayList<>();

	@OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
	@MergeIgnore
	private List<APIIdentifier> apiIdentifiers = new ArrayList<>();

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

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

	public String getFirstAiredReadable()
	{
		if(firstAired == null)
		{
			return null;
		}
		return firstAired.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public String getImagePath(MediaItemImageType mediaItemImageType)
	{
		return switch(mediaItemImageType)
		{
			case BANNER -> getBannerPath();
			case POSTER -> getPosterPath();
		};
	}

	public void setImagePath(MediaItemImageType mediaItemImageType, String path)
	{
		switch(mediaItemImageType)
		{
			case BANNER -> setBannerPath(path);
			case POSTER -> setPosterPath(path);
			default -> throw new UnsupportedOperationException("Image type not implemented");
		}
	}

	public Optional<APIIdentifier> getApiIdentifierByType(APIType apiType)
	{
		return apiIdentifiers.stream()
				.filter(i -> i.getType().equals(apiType))
				.findFirst();
	}

	public void addSeason(Season season)
	{
		if(seasons == null)
		{
			seasons = new ArrayList<>();
		}
		seasons.add(season);
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Show show)) return false;
		return Objects.equals(id, show.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}
}
