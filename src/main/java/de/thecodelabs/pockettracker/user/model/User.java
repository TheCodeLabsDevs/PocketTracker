package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.user.model.authentication.ApiTokenAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "appuser")
@Getter
@Setter
public class User
{
	@Id
	private UUID id;

	@NotNull
	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserAuthentication> authentications = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ApiTokenAuthentication> tokens = new ArrayList<>();

	@NotNull
	private UserRole userRole;

	@OneToOne(cascade = CascadeType.ALL)
	private UserSettings settings;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<HiddenShow> hiddenShows;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<WatchedEpisode> watchedEpisodes;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<AddedMovie> movies;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

	public <T extends UserAuthentication> Optional<T> getAuthentication(Class<T> type)
	{
		return authentications.stream()
				.filter(authentication -> authentication.getClass().equals(type))
				.map(authentication -> (T) authentication)
				.findFirst();
	}

	public void addAuthentication(UserAuthentication userAuthentication)
	{
		this.authentications.add(userAuthentication);
	}

	public void addAllHiddenShows(List<HiddenShow> newHiddenShows)
	{
		if(this.hiddenShows == null)
		{
			this.hiddenShows = new ArrayList<>();
		}
		this.hiddenShows.addAll(newHiddenShows);
	}

	public boolean isShowHidden(UUID id)
	{
		return this.hiddenShows.stream().anyMatch(show -> show.getShow().getId().equals(id));
	}

	public void addWatchedEpisodes(List<WatchedEpisode> newEpisodes)
	{
		if(this.watchedEpisodes == null)
		{
			this.watchedEpisodes = new ArrayList<>();
		}
		this.watchedEpisodes.addAll(newEpisodes);
	}

	public void addMovies(List<AddedMovie> newMovies)
	{
		if(this.movies == null)
		{
			this.movies = new ArrayList<>();
		}
		this.movies.addAll(newMovies);
	}

	public Optional<AddedMovie> getMovieById(UUID id)
	{
		return this.movies.stream()
				.filter(movie -> movie.getMovie().getId().equals(id))
				.findFirst();
	}

	@Override
	public String toString()
	{
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", userRole=" + userRole +
				'}';
	}
}
