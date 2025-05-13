package de.thecodelabs.pockettracker.user.service;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.movie.MovieService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import de.thecodelabs.pockettracker.show.model.ShowType;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.StatisticItem;
import de.thecodelabs.pockettracker.user.controller.UserForm;
import de.thecodelabs.pockettracker.user.model.*;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import de.thecodelabs.pockettracker.user.repository.*;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService
{
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final GitlabAuthenticationRepository gitlabAuthenticationRepository;

	private final ShowService showService;
	private final MessageSource messageSource;

	private final HiddenShowRepository hiddenShowRepository;
	private final WatchedEpisodeRepository watchedEpisodeRepository;

	private final UserAddedMovieRepository userAddedMovieRepository;

	private final MovieService movieService;

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

	public Optional<User> getUserById(UUID id)
	{
		return userRepository.findById(id);
	}

	public Optional<User> getUserByUserAuthentication(UserAuthentication userAuthentication)
	{
		return userRepository.findUserByAuthenticationsContains(userAuthentication);
	}

	@Transactional
	public UserSettings getUserSettings()
	{
		final User currentUser = getCurrentUser();
		if(currentUser.getSettings() == null)
		{
			currentUser.setSettings(new UserSettings(currentUser));
			return userRepository.save(currentUser).getSettings();
		}
		return currentUser.getSettings();
	}

	public List<User> getUsers()
	{
		return userRepository.findAll().stream().sorted(Comparator.comparing(User::getId)).toList();
	}

	public void createUser(User user)
	{
		userRepository.save(user);
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

	public void editUser(User user, UserForm userForm) throws PasswordValidationException
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
		userRepository.save(user);
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

	public void saveUser(User user)
	{
		userRepository.save(user);
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

		return userForm.getPassword().equals(userForm.getPasswordRepeat());
	}

	public List<WatchedEpisode> getWatchedEpisodesByShow(User user, Show show)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watched -> watched.getEpisode().getSeason().getShow().equals(show))
				.toList();
	}

	public List<WatchedEpisode> getWatchedEpisodesBySeason(User user, Season season)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watched -> watched.getEpisode().getSeason().equals(season))
				.toList();
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
			watchedEpisodeRepository.deleteWatchedEpisode(watchedEpisode.getEpisode().getId(), watchedEpisode.getUser().getId());
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

	public Integer getTotalPlayedMinutes(User user, @Nullable ShowType showType)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watched -> showType == null || watched.getEpisode().getSeason().getShow().getType() == showType)
				.filter(watched -> watched.getEpisode().getLengthInMinutes() != null)
				.mapToInt(watched -> watched.getEpisode().getLengthInMinutes())
				.sum();
	}

	public Integer getNumberOfCompletedSeasons(User user)
	{
		final List<Season> seasonsWithAtLeastOneEpisodeWatched = user.getWatchedEpisodes().stream()
				.map(watched -> watched.getEpisode().getSeason())
				.distinct()
				.toList();

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
				.toList();

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

	private Integer getTotalPlayedMovieMinutes(User user)
	{
		return user.getMovies().stream()
				.mapToInt(watched -> watched.getMovie().getLengthInMinutes())
				.sum();
	}

	public List<StatisticItem> getGeneralStatistics(User user)
	{
		final List<StatisticItem> statisticItems = new ArrayList<>();
		statisticItems.add(new StatisticItem("fas fa-tv", messageSource.getMessage("statistics.general.shows", new Object[]{user.getHiddenShows().size()}, LocaleContextHolder.getLocale()), BootstrapColor.INFO, BootstrapColor.DARK));
		statisticItems.add(new StatisticItem("fas fa-tv", messageSource.getMessage("statistics.general.shows.complete", new Object[]{getNumberOfCompletedShows(user)}, LocaleContextHolder.getLocale()), BootstrapColor.PRIMARY, BootstrapColor.LIGHT));
		statisticItems.add(new StatisticItem("fas fa-folder", messageSource.getMessage("statistics.general.seasons.complete", new Object[]{getNumberOfCompletedSeasons(user)}, LocaleContextHolder.getLocale()), BootstrapColor.SUCCESS, BootstrapColor.LIGHT));
		statisticItems.add(new StatisticItem("far fa-file", messageSource.getMessage("statistics.general.episodes", new Object[]{user.getWatchedEpisodes().size()}, LocaleContextHolder.getLocale()), BootstrapColor.DARK, BootstrapColor.LIGHT));
		statisticItems.add(new StatisticItem("fas fa-film", messageSource.getMessage("statistics.general.movies", new Object[]{user.getMovies().size()}, LocaleContextHolder.getLocale()), BootstrapColor.SECONDARY, BootstrapColor.LIGHT));
		return statisticItems;
	}

	public List<StatisticItem> getWatchTimeStatistics(User user)
	{
		final List<StatisticItem> statisticItems = new ArrayList<>();

		final Integer totalPlayedMinutesTv = getTotalPlayedMinutes(user, ShowType.TV);
		final Integer totalPlayedMinutesMovies = getTotalPlayedMovieMinutes(user);
		final Integer totalPlayedMinutes = totalPlayedMinutesTv + totalPlayedMinutesMovies;
		final String timeStatisticsTv = messageSource.getMessage("statistics.timeBased.tv", new Object[]{totalPlayedMinutes, totalPlayedMinutes / 60, totalPlayedMinutes / 60 / 24}, LocaleContextHolder.getLocale());
		statisticItems.add(new StatisticItem("fas fa-video", timeStatisticsTv, BootstrapColor.DANGER, BootstrapColor.LIGHT));

		final Integer totalPlayedMinutesAudio = getTotalPlayedMinutes(user, ShowType.AUDIO);
		final String timeStatisticsAudio = messageSource.getMessage("statistics.timeBased.audio", new Object[]{totalPlayedMinutesAudio, totalPlayedMinutesAudio / 60, totalPlayedMinutesAudio / 60 / 24}, LocaleContextHolder.getLocale());
		statisticItems.add(new StatisticItem("fas fa-music", timeStatisticsAudio, BootstrapColor.WARNING, BootstrapColor.DARK));

		return statisticItems;
	}

	@Transactional
	public void deleteWatchedShow(Show show)
	{
		final List<User> users = userRepository.findAll();
		for(User user : users)
		{
			removeHiddenShowFromUser(user, show);
		}

		for(Season season : show.getSeasons())
		{
			deleteWatchedSeason(season);
		}
	}

	@Transactional
	public void deleteWatchedSeason(Season season)
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
		watchedEpisodes.forEach(e ->
				watchedEpisodeRepository.deleteWatchedEpisode(e.getEpisode().getId(), e.getUser().getId()));
	}

	public void removeHiddenShowFromUser(User user, Show show)
	{
		final List<HiddenShow> hiddenShows = user.getHiddenShows().stream()
				.filter(s -> s.getShow().equals(show))
				.toList();

		if(hiddenShows.isEmpty())
		{
			return;
		}

		for(HiddenShow hiddenShow : hiddenShows)
		{
			hiddenShowRepository.deleteHiddenShow(hiddenShow.getShow().getId(), hiddenShow.getUser().getId());
		}
	}

	public void removeAllWatchedEpisodesFromUser(User user, Show show)
	{
		final List<WatchedEpisode> watchedEpisodesByShow = getWatchedEpisodesByShow(user, show);
		if(watchedEpisodesByShow.isEmpty())
		{
			return;
		}

		watchedEpisodesByShow.forEach(episode ->
				watchedEpisodeRepository.deleteWatchedEpisode(episode.getEpisode().getId(), episode.getUser().getId()));
	}

	@Transactional
	public void deleteWatchedMovie(Movie movie)
	{
		final List<User> users = userRepository.findAll();
		for(User user : users)
		{
			removeMovieFromUser(user, movie);
		}
	}

	public boolean removeMovieFromUser(User user, Movie movie)
	{
		final List<AddedMovie> addedMovieList = user.getMovies().stream()
				.filter(addedMovie -> addedMovie.getMovie().equals(movie))
				.toList();

		if(addedMovieList.isEmpty())
		{
			return false;
		}

		for(AddedMovie addedMovie : addedMovieList)
		{
			userAddedMovieRepository.deleteAddedMovie(addedMovie.getMovie().getId(), addedMovie.getUser().getId());
		}
		return true;
	}

	public String getWatchDateForMovie(User user, Movie movie)
	{
		return user.getMovies().stream()
				.filter(m -> m.getMovie().equals(movie))
				.findFirst()
				.map(value -> value.getWatchedDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
				.orElse(null);
	}

	public boolean isMovieAdded(User user, UUID movieId)
	{
		return user.getMovieById(movieId).isPresent();
	}

	public void toggleShowHidden(Show show)
	{
		final User currentUser = getCurrentUser();
		final Optional<HiddenShow> hiddenShowOptional = currentUser.getHiddenShows().stream().filter(s -> s.getShow().equals(show)).findFirst();
		hiddenShowOptional.ifPresent(hiddenShow -> hiddenShowRepository.deleteHiddenShow(show.getId(), currentUser.getId()));

		hiddenShowRepository.save(new HiddenShow(currentUser, show));
	}

	@Transactional
	public void prepareShowsModel(Model model, String searchText)
	{
		final UserSettings settings = getUserSettings();
		final Boolean showHiddenShows = Optional.ofNullable(settings.getShowHiddenShows())
				.orElse(false);
		final ShowFilterOption filterOption = Optional.ofNullable(settings.getLastShowFilterOption())
				.orElse(ShowFilterOption.ALL_SHOWS);
		final ShowSortOption sortOption = Optional.ofNullable(settings.getLastShowSortOption())
				.orElse(ShowSortOption.LAST_WATCHED);

		final User user = getCurrentUser();

		final List<Show> shows = showService.getAll(searchText);
		Stream<Show> filteredShows = filterOption.getFilter().filter(shows, user);
		if(Boolean.FALSE.equals(showHiddenShows))
		{
			filteredShows = filteredShows.filter(show -> !user.isShowHidden(show.getId()));
		}
		final List<Show> sortedShows = sortOption.getSorter().sort(filteredShows, user);

		model.addAttribute("shows", sortedShows);
		model.addAttribute("showHiddenShows", showHiddenShows);
		model.addAttribute("currentFilterOption", filterOption);
		model.addAttribute("currentSortOption", sortOption);
		model.addAttribute("numberOfAllShows", showService.getAll(null).size());
		model.addAttribute("currentPage", "Meine Serien");
	}

	public void prepareMoviesModel(Model model, String searchText)
	{
		final List<Movie> movies = movieService.getAll(searchText);
		final List<Movie> sortedMovies = movies.stream().sorted(Comparator.comparing(m -> m.getName().toLowerCase())).toList();

		model.addAttribute("movies", sortedMovies);
		model.addAttribute("currentPage", "Meine Filme");
		model.addAttribute("numberOfAllMovies", movieService.getAll(null).size());
	}
}
