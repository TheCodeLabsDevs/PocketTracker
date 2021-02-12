package de.thecodelabs.pockettracker.user.service;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.StatisticItem;
import de.thecodelabs.pockettracker.user.controller.UserForm;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import de.thecodelabs.pockettracker.user.repository.GitlabAuthenticationRepository;
import de.thecodelabs.pockettracker.user.repository.UserRepository;
import de.thecodelabs.pockettracker.user.repository.WatchedEpisodeRepository;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;
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
	private final GitlabAuthenticationRepository gitlabAuthenticationRepository;

	private final ShowService showService;
	private final WatchedEpisodeRepository watchedEpisodeRepository;

	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ShowService showService,
					   GitlabAuthenticationRepository gitlabAuthenticationRepository, WatchedEpisodeRepository watchedEpisodeRepository)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.showService = showService;
		this.gitlabAuthenticationRepository = gitlabAuthenticationRepository;
		this.watchedEpisodeRepository = watchedEpisodeRepository;
	}

	public Optional<User> getCurrentUserOptional()
	{
		return getUser(SecurityContextHolder.getContext().getAuthentication());
	}

	public User getCurrentUser()
	{
		return getUser(SecurityContextHolder.getContext().getAuthentication())
				.orElseThrow(() -> new NotFoundException("User not found"));
	}

	public Optional<User> getUser(Authentication authentication)
	{
		if(authentication == null)
		{
			return Optional.empty();
		}

		if(authentication instanceof OAuth2AuthenticationToken)
		{
			return getUserByGitlabAuthentication(authentication.getName());
		}
		else if(authentication instanceof UsernamePasswordAuthenticationToken ||
				authentication instanceof RememberMeAuthenticationToken)
		{
			return getUserByInternalAuthentication(authentication.getName());
		}
		else if(authentication instanceof PreAuthenticatedAuthenticationToken)
		{
			return getUserByAccessToken((String) authentication.getCredentials());
		}
		return Optional.empty();
	}

	public Optional<User> getUserByAccessToken(String token)
	{
		return userRepository.findUserByTokens_token(token);
	}

	public Optional<User> getUserByGitlabAuthentication(String username)
	{
		return gitlabAuthenticationRepository.findByGitlabUsername(username).map(UserAuthentication::getUser);
	}

	public Optional<User> getUserByInternalAuthentication(String username)
	{
		return userRepository.findUserByName(username).filter(user -> user.getAuthentication(InternalAuthentication.class).isPresent());
	}

	public Optional<User> getUserById(Integer id)
	{
		return userRepository.findById(id);
	}

	public Optional<User> getUserByUserAuthentication(UserAuthentication userAuthentication)
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
		authentication.setUser(user);
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
		authentication.setUser(user);
		userRepository.save(user);
	}

	public User editUser(User user, UserForm userForm) throws PasswordValidationException
	{
		user.setName(userForm.getUsername());

		// Admin only changes
		getCurrentUserOptional().filter(u -> u.getUserRole() == UserRole.ADMIN).ifPresent(u -> {
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

	public List<WatchedEpisode> getWatchedEpisodesByShow(User user, Show show)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watched -> watched.getEpisode().getSeason().getShow().equals(show))
				.collect(Collectors.toList());
	}

	public List<WatchedEpisode> getWatchedEpisodesBySeason(User user, Season season)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watched -> watched.getEpisode().getSeason().equals(season))
				.collect(Collectors.toList());
	}

	public boolean isWatchedEpisode(User user, Episode episode)
	{
		return user.getWatchedEpisodes().stream().anyMatch(watched -> watched.getEpisode().equals(episode));
	}

	@Transactional
	public void addWatchedEpisode(User user, Episode episode)
	{
		WatchedEpisode watchedEpisode = new WatchedEpisode(user, episode, LocalDate.now());
		user.getWatchedEpisodes().add(watchedEpisode);
	}

	@Transactional
	public void removeWatchedEpisode(User user, Episode episode)
	{
		final Optional<WatchedEpisode> watchedEpisodeOptional = user.getWatchedEpisodes().stream()
				.filter(watched -> watched.getEpisode().equals(episode))
				.findFirst();

		watchedEpisodeOptional.ifPresent(watchedEpisode -> {
			user.getWatchedEpisodes().remove(watchedEpisode);
			watchedEpisodeRepository.delete(watchedEpisode);
		});
	}

	@Transactional
	public void toggleCompleteSeason(User user, Season season, boolean markAsWatched)
	{
		for(Episode episode : season.getEpisodes())
		{
			if(isWatchedEpisode(user, episode))
			{
				// is watched but should be marked as unwatched
				if(!markAsWatched)
				{
					removeWatchedEpisode(user, episode);
				}
			}
			else
			{
				// is unwatched but should be marked as watched
				if(markAsWatched)
				{
					addWatchedEpisode(user, episode);
				}
			}
		}
	}

	public Integer getTotalPlayedMinutes(User user)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watched -> watched.getEpisode().getLengthInMinutes() != null)
				.mapToInt(watched -> watched.getEpisode().getLengthInMinutes())
				.sum();
	}

	public Integer getNumberOfCompletedSeasons(User user)
	{
		final List<Season> seasonsWithAtLeastOneEpisodeWatched = user.getWatchedEpisodes().stream()
				.map(watched -> watched.getEpisode().getSeason())
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
				.map(watched -> watched.getEpisode().getSeason().getShow())
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
		statisticItems.add(new StatisticItem("fas fa-tv", MessageFormat.format("{0} Serien", user.getShows().size()), BootstrapColor.PRIMARY, BootstrapColor.LIGHT));
		statisticItems.add(new StatisticItem("fas fa-tv", MessageFormat.format("{0} Serien komplett", getNumberOfCompletedShows(user)), BootstrapColor.SUCCESS, BootstrapColor.LIGHT));
		statisticItems.add(new StatisticItem("fas fa-folder", MessageFormat.format("{0} Staffeln komplett", getNumberOfCompletedSeasons(user)), BootstrapColor.WARNING, BootstrapColor.DARK));
		statisticItems.add(new StatisticItem("fas fa-film", MessageFormat.format("{0} Episoden", user.getWatchedEpisodes().size()), BootstrapColor.DARK, BootstrapColor.LIGHT));
		statisticItems.add(new StatisticItem("fas fa-hourglass", MessageFormat.format("{0} Minuten", getTotalPlayedMinutes(user)), BootstrapColor.DANGER, BootstrapColor.LIGHT));

		return statisticItems;
	}

	@Transactional
	public void deleteShow(Show show)
	{
		final List<User> users = userRepository.findAll();
		for(User user : users)
		{
			user.getShows().remove(show);
		}

		for(Season season : show.getSeasons())
		{
			deleteSeason(season);
		}
	}

	@Transactional
	public void deleteSeason(Season season)
	{
		for(Episode episode : season.getEpisodes())
		{
			deleteWatchedEpisodes(episode);
		}
	}

	@Transactional
	public void deleteWatchedEpisodes(Episode episode)
	{
		final List<WatchedEpisode> watchedEpisodes = episode.getWatchedEpisodes();
		watchedEpisodeRepository.deleteAll(watchedEpisodes);
	}
}
