package de.thecodelabs.pockettracker.movie.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import de.thecodelabs.pockettracker.utils.json.JsonResourcePathSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
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

	public void setId(UUID id)
	{
		this.id = id;
	}

	public UUID getId()
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

	public LocalDate getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public String getPosterPath()
	{
		return posterPath;
	}

	public void setPosterPath(String posterPath)
	{
		this.posterPath = posterPath;
	}

	public List<APIIdentifier> getApiIdentifiers()
	{
		return apiIdentifiers;
	}

	public void setApiIdentifiers(List<APIIdentifier> apiIdentifiers)
	{
		this.apiIdentifiers = apiIdentifiers;
	}

	public Integer getLengthInMinutes()
	{
		return lengthInMinutes;
	}

	public void setLengthInMinutes(Integer lengthInMinutes)
	{
		this.lengthInMinutes = lengthInMinutes;
	}

	public List<AddedMovie> getFavoriteUsers()
	{
		return favoriteUsers;
	}

	public void setFavoriteUsers(List<AddedMovie> favoriteUsers)
	{
		this.favoriteUsers = favoriteUsers;
	}

	public String getImagePath(MovieImageType movieImageType)
	{
		return switch(movieImageType)
		{
			case POSTER -> getPosterPath();
		};
	}

	public void setImagePath(MovieImageType movieImageType, String path)
	{
		if(Objects.requireNonNull(movieImageType) != MovieImageType.POSTER)
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

	@Override
	public boolean equals(Object o)
	{
		if(o == null || getClass() != o.getClass()) return false;
		Movie movie = (Movie) o;
		return Objects.equals(id, movie.id) && Objects.equals(name, movie.name) && Objects.equals(description, movie.description) && Objects.equals(releaseDate, movie.releaseDate) && Objects.equals(posterPath, movie.posterPath) && Objects.equals(lengthInMinutes, movie.lengthInMinutes) && Objects.equals(apiIdentifiers, movie.apiIdentifiers);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, description, releaseDate, posterPath, lengthInMinutes, favoriteUsers, apiIdentifiers);
	}

	@Override
	public String toString()
	{
		return "Movie{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", releaseDate=" + releaseDate +
				", posterPath='" + posterPath + '\'' +
				", lengthInMinutes=" + lengthInMinutes +
				", apiIdentifiers=" + apiIdentifiers +
				'}';
	}
}
