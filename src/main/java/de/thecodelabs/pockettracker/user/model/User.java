package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.authentication.ApiTokenAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "appuser")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<UserAuthentication> authentications = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ApiTokenAuthentication> tokens = new ArrayList<>();

	@NotNull
	private UserRole userRole;

	@ManyToMany
	private List<Show> shows;

	@ManyToMany
	private List<Episode> watchedEpisodes;

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

	public void addAuthentication(UserAuthentication userAuthentication) {
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

	public List<Show> getShows()
	{
		return shows;
	}

	public List<Episode> getWatchedEpisodes()
	{
		return watchedEpisodes;
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
