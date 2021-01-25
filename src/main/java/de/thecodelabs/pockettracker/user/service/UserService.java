package de.thecodelabs.pockettracker.user.service;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.season.Season;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.StatisticItem;
import de.thecodelabs.pockettracker.user.controller.UserForm;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import de.thecodelabs.pockettracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService
{
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final ShowService showService;

	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ShowService showService)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.showService = showService;
	}

	public Optional<User> getCurrentUser()
	{
		return getUser(SecurityContextHolder.getContext().getAuthentication());
	}

	public Optional<User> getUser(Authentication authentication)
	{
		if(authentication == null)
		{
			return Optional.empty();
		}

		if(authentication instanceof OAuth2AuthenticationToken)
		{
			// Get Gitlab user or create new account
			return getUser(authentication.getName(), GitlabAuthentication.class).or(() -> {

				final User user = getUser(authentication.getName()).orElseGet(() -> {
					final UserForm userForm = new UserForm();
					userForm.setUsername(authentication.getName());
					return createUser(userForm);
				});
				addGitlabAuthentication(user, authentication.getName());
				return Optional.of(user);
			});
		}
		else if(authentication instanceof UsernamePasswordAuthenticationToken)
		{
			return getUser(authentication.getName(), InternalAuthentication.class);
		}
		else if(authentication instanceof RememberMeAuthenticationToken)
		{
			return getUser(authentication.getName(), InternalAuthentication.class);
		}
		return Optional.empty();
	}

	public Optional<User> getUser(String username, Class<? extends UserAuthentication> type)
	{
		return getUser(username).filter(user -> user.getAuthentication(type).isPresent());
	}

	public Optional<User> getUser(String username)
	{
		return userRepository.findUserByName(username);
	}

	public Optional<User> getUser(Integer id)
	{
		return userRepository.findById(id);
	}

	public Optional<User> getUserByAuthentication(UserAuthentication userAuthentication)
	{
		return userRepository.findUserByAuthenticationsContains(userAuthentication);
	}

	public List<User> getUsers()
	{
		return userRepository.findAll().stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
	}

	public User createUser(UserForm userForm)
	{
		User user = new User();
		user.setName(userForm.getUsername());
		user.setUserRole(userForm.getUserRole());

		if(user.getUserRole() == null)
		{
			user.setUserRole(UserRole.USER);
		}

		return userRepository.save(user);
	}

	public void addGitlabAuthentication(User user, String name)
	{
		GitlabAuthentication authentication = new GitlabAuthentication();
		authentication.setGitlabUsername(name);
		user.addAuthentication(authentication);
		userRepository.save(user);
	}

	public void addInternalAuthentication(User user, UserForm form) throws PasswordValidationException
	{
		if(!validatePassword(form))
		{
			throw new PasswordValidationException();
		}
		InternalAuthentication authentication = new InternalAuthentication();
		authentication.setPassword(passwordEncoder.encode(form.getPassword()));
		user.addAuthentication(authentication);
		userRepository.save(user);
	}

	public User editUser(User user, UserForm userForm) throws PasswordValidationException
	{
		user.setName(userForm.getUsername());

		// Admin only changes
		getCurrentUser().filter(u -> u.getUserRole() == UserRole.ADMIN).ifPresent(u -> {
			if(userForm.getUserRole() != null)
			{
				user.setUserRole(userForm.getUserRole());
			}
		});

		final String password = userForm.getPassword();
		if(password != null && !password.isEmpty())
		{
			final Optional<InternalAuthentication> authentication = user.getAuthentication(InternalAuthentication.class);
			if(authentication.isPresent())
			{
				editInternalAuthentication(authentication.get(), userForm);
			}
			else
			{
				addInternalAuthentication(user, userForm);
			}
		}
		return userRepository.save(user);
	}

	public void editInternalAuthentication(InternalAuthentication authentication, UserForm form) throws PasswordValidationException
	{
		if(!validatePassword(form))
		{
			throw new PasswordValidationException();
		}

		authentication.setPassword(passwordEncoder.encode(form.getPassword()));
	}

	public boolean hasUsers()
	{
		return userRepository.count() > 0;
	}

	public void deleteUser(User user)
	{
		userRepository.delete(user);
	}

	private boolean validatePassword(UserForm userForm)
	{
		if(userForm.getPassword().isEmpty())
		{
			return false;
		}

		if(!userForm.getPassword().equals(userForm.getPasswordRepeat()))
		{
			return false;
		}
		return true;
	}

	public List<Episode> getWatchedEpisodesByShow(User user, Show show)
	{
		return user.getWatchedEpisodes().stream()
				.filter(episode -> episode.getSeason().getShow().equals(show))
				.collect(Collectors.toList());
	}

	public List<Episode> getWatchedEpisodesBySeason(User user, Season season)
	{
		return user.getWatchedEpisodes().stream()
				.filter(episode -> episode.getSeason().equals(season))
				.collect(Collectors.toList());
	}

	public boolean isWatchedEpisode(User user, Episode episode)
	{
		return user.getWatchedEpisodes().contains(episode);
	}

	public void toggleCompleteSeason(User user, Season season, boolean markAsWatched)
	{
		for(Episode episode : season.getEpisodes())
		{
			if(isWatchedEpisode(user, episode))
			{
				// is watched but should be marked as unwatched
				if(!markAsWatched)
				{
					user.getWatchedEpisodes().remove(episode);
				}
			}
			else
			{
				// is unwatched but should be marked as watched
				if(markAsWatched)
				{
					user.getWatchedEpisodes().add(episode);
				}
			}
		}
	}

	public Integer getTotalPlayedMinutes(User user)
	{
		return user.getWatchedEpisodes().stream()
				.mapToInt(Episode::getLengthInMinutes)
				.sum();
	}

	public Integer getNumberOfCompletedSeasons(User user)
	{
		final List<Season> seasonsWithAtLeastOneEpisodeWatched = user.getWatchedEpisodes().stream()
				.map(Episode::getSeason)
				.distinct()
				.collect(Collectors.toList());

		Integer completelyWatchedSeasons = 0;
		for(Season season : seasonsWithAtLeastOneEpisodeWatched)
		{
			final int numberOfWatchedEpisodes = getWatchedEpisodesBySeason(user, season).size();
			if(numberOfWatchedEpisodes == season.getEpisodes().size())
			{
				completelyWatchedSeasons++;
			}
		}

		return completelyWatchedSeasons;
	}

	public Integer getNumberOfCompletedShows(User user)
	{
		final List<Show> showsWithAtLeastOneEpisodeWatched = user.getWatchedEpisodes().stream()
				.map(episode -> episode.getSeason().getShow())
				.distinct()
				.collect(Collectors.toList());

		Integer completelyWatchedShows = 0;
		for(Show show : showsWithAtLeastOneEpisodeWatched)
		{
			final int numberOfWatchedEpisodes = getWatchedEpisodesByShow(user, show).size();
			if(numberOfWatchedEpisodes == showService.getTotalNumberOfEpisodes(show))
			{
				completelyWatchedShows++;
			}
		}

		return completelyWatchedShows;
	}

	public List<StatisticItem> getStatistics(User user)
	{
		final List<StatisticItem> statisticItems = new ArrayList<>();
		statisticItems.add(new StatisticItem("fas fa-tv", MessageFormat.format("{0} Serien", user.getShows().size())));
		statisticItems.add(new StatisticItem("fas fa-tv", MessageFormat.format("{0} Serien komplett", getNumberOfCompletedShows(user))));
		statisticItems.add(new StatisticItem("fas fa-folder", MessageFormat.format("{0} Staffeln komplett", getNumberOfCompletedSeasons(user))));
		statisticItems.add(new StatisticItem("fas fa-film", MessageFormat.format("{0} Episoden", user.getWatchedEpisodes().size())));
		statisticItems.add(new StatisticItem("fas fa-hourglass", MessageFormat.format("{0} Minuten", getTotalPlayedMinutes(user))));

		return statisticItems;
	}
}
