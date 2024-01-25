package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.user.model.authentication.ApiTokenAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "appuser")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
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
	private List<AddedShow> shows;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<WatchedEpisode> watchedEpisodes;

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

	public List<UserAuthentication> getAuthentications()
	{
		return authentications;
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

	public void setAuthentications(List<UserAuthentication> authentications)
	{
		this.authentications = authentications;
	}

	public List<ApiTokenAuthentication> getTokens()
	{
		return tokens;
	}

	public void setTokens(List<ApiTokenAuthentication> tokens)
	{
		this.tokens = tokens;
	}

	public UserRole getUserRole()
	{
		return userRole;
	}

	public void setUserRole(UserRole userRole)
	{
		this.userRole = userRole;
	}

	public UserSettings getSettings()
	{
		return settings;
	}

	public void setSettings(UserSettings settings)
	{
		this.settings = settings;
	}

	public List<AddedShow> getShows()
	{
		return shows;
	}

	public void addShows(List<AddedShow> newShows)
	{
		if(this.shows == null)
		{
			this.shows = new ArrayList<>();
		}
		this.shows.addAll(newShows);
	}

	public Optional<AddedShow> getShowById(UUID id)
	{
		return this.shows.stream()
				.filter(show -> show.getShow().getId().equals(id))
				.findFirst();
	}

	public List<WatchedEpisode> getWatchedEpisodes()
	{
		return watchedEpisodes;
	}

	public void addWatchedEpisodes(List<WatchedEpisode> newEpisodes)
	{
		if(this.watchedEpisodes == null)
		{
			this.watchedEpisodes = new ArrayList<>();
		}
		this.watchedEpisodes.addAll(newEpisodes);
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
