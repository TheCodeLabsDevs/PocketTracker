package de.thecodelabs.pockettracker.movie.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.mediaitem.MediaItemImageType;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import de.thecodelabs.pockettracker.utils.json.JsonResourcePathSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
@EqualsAndHashCode
@ToString
public class Movie
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
	private LocalDate releaseDate;

	@Column(length = 2048)
	@JsonView(View.Summary.class)
	@JsonSerialize(using = JsonResourcePathSerializer.class)
	private String posterPath;

	@NotNull
	@Min(0)
	private Integer lengthInMinutes;

	@OneToMany(mappedBy = "movie")
	@MergeIgnore
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private List<AddedMovie> favoriteUsers = new ArrayList<>();

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
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

	public Movie()
	{
	}

	public Movie(@NotNull String name, String description, LocalDate releaseDate, String posterPath, Integer lengthInMinutes)
	{
		this.name = name;
		this.description = description;
		this.releaseDate = releaseDate;
		this.posterPath = posterPath;
		this.lengthInMinutes = lengthInMinutes;
	}

	public String getImagePath(MediaItemImageType mediaItemImageType)
	{
		if(Objects.requireNonNull(mediaItemImageType) != MediaItemImageType.POSTER)
		{
			throw new UnsupportedOperationException("Image type not implemented");
		}

		return getPosterPath();
	}

	public void setImagePath(MediaItemImageType mediaItemImageType, String path)
	{
		if(Objects.requireNonNull(mediaItemImageType) != MediaItemImageType.POSTER)
		{
			throw new UnsupportedOperationException("Image type not implemented");
		}

		setPosterPath(path);
	}

	public Optional<APIIdentifier> getApiIdentifierByType(APIType apiType)
	{
		return apiIdentifiers.stream()
				.filter(i -> i.getType().equals(apiType))
				.findFirst();
	}

	public String getReleaseDateReadable()
	{
		if(releaseDate == null)
		{
			return null;
		}
		return releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}
