package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.movie.MovieService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.SeasonRepository;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserSettings;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.navigation.UserNavigationCoordinator;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static de.thecodelabs.pockettracker.MainController.PARAMETER_NAME_IS_USER_SPECIFIC_VIEW;
import static de.thecodelabs.pockettracker.MainController.PARAMETER_NAME_SEARCH_TEXT;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController
{
	private final UserService userService;

	private final ShowService showService;
	private final SeasonRepository seasonRepository;
	private final EpisodeRepository episodeRepository;
	private final MovieService movieService;

	private static class ReturnValues
	{
		public static final String INDEX = "index";
		public static final String REDIRECT_USER_SHOWS = "redirect:/user/shows";
		public static final String REDIRECT_SHOWS = "redirect:/shows";
		public static final String REDIRECT_MOVIES = "redirect:/movies";
		public static final String REDIRECT_USER_MOVIES = "redirect:/user/movies";
		public static final String MOVIES = "movies";
	}

	@PostMapping("/shows")
	@Transactional
	public String postFilterSubmission(@RequestParam ShowSortOption sortOption,
									   @RequestParam ShowFilterOption filterOption,
									   @RequestParam Boolean showDislikedShows)
	{
		final UserSettings settings = userService.getUserSettings();
		settings.setLastShowSortOption(sortOption);
		settings.setLastShowFilterOption(filterOption);
		settings.setShowDislikedShows(showDislikedShows);

		return ReturnValues.REDIRECT_USER_SHOWS;
	}

	@GetMapping("/shows")
	public String getShows(WebRequest request, Model model)
	{
		UserNavigationCoordinator.setUserSpecificNavigation(request, true);

		final UserSettings settings = userService.getUserSettings();
		final Boolean showDislikedShows = Optional.ofNullable(settings.getShowDislikedShows())
				.orElse(false);
		final ShowFilterOption filterOption = Optional.ofNullable(settings.getLastShowFilterOption())
				.orElse(ShowFilterOption.ALL_SHOWS);
		final ShowSortOption sortOption = Optional.ofNullable(settings.getLastShowSortOption())
				.orElse(ShowSortOption.LAST_WATCHED);

		final User user = userService.getCurrentUser();

		String searchText = null;
		if(model.containsAttribute(PARAMETER_NAME_SEARCH_TEXT))
		{
			searchText = (String) model.getAttribute(PARAMETER_NAME_SEARCH_TEXT);
		}

		final List<Show> shows = showService.getAllFavoriteByUser(searchText, user);
		Stream<Show> filteredShows = filterOption.getFilter().filter(shows, user);
		if(Boolean.FALSE.equals(showDislikedShows))
		{
			filteredShows = filteredShows.filter(show -> !user.getShowById(show.getId())
					.map(AddedShow::getDisliked)
					.orElse(false));
		}
		final List<Show> sortedShows = sortOption.getSorter().sort(filteredShows, user);

		model.addAttribute("shows", sortedShows);
		model.addAttribute("showDislikedShows", showDislikedShows);
		model.addAttribute("currentFilterOption", filterOption);
		model.addAttribute("currentSortOption", sortOption);

		model.addAttribute("currentPage", "Meine Serien");
		model.addAttribute("userShows", user.getShows().stream().map(AddedShow::getShow).toList());
		model.addAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, true);

		return ReturnValues.INDEX;
	}

	@GetMapping("/shows/add/{showId}")
	@Transactional
	public String addShow(WebRequest request, @PathVariable UUID showId)
	{
		final Optional<Show> showOptional = showService.getById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_SHOWS;
		}

		final User user = userService.getCurrentUser();
		user.getShows().add(new AddedShow(user, showOptional.get(), false));

		return ReturnValues.REDIRECT_SHOWS;
	}

	@GetMapping("/shows/remove/{showId}")
	public String removeShow(WebRequest request, @PathVariable UUID showId)
	{
		final Optional<Show> showOptional = showService.getById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_SHOWS;
		}

		final User user = userService.getCurrentUser();
		final Show showToRemove = showOptional.get();
		final boolean userHadShow = userService.removeShowFromUser(user, showToRemove);
		if(!userHadShow)
		{
			WebRequestUtils.putToast(request, new Toast("Der Nutzer hatte die Serie nie hinzugefügt.", BootstrapColor.WARNING));
			return ReturnValues.REDIRECT_USER_SHOWS;
		}

		userService.removeAllWatchedEpisodesFromUser(user, showToRemove);
		return ReturnValues.REDIRECT_USER_SHOWS;
	}

	@GetMapping("/shows/dislike/{showId}")
	@Transactional
	public String dislikeShow(WebRequest request, @PathVariable UUID showId)
	{
		final Optional<AddedShow> showOptional = userService.getCurrentUser().getShowById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_SHOWS;
		}

		final AddedShow addedShow = showOptional.get();
		addedShow.setDisliked(!Optional.ofNullable(addedShow.getDisliked()).orElse(false));

		return "redirect:/show/" + showId;
	}

	@GetMapping("/season/{seasonId}")
	@Transactional
	public String setSeasonAsWatched(WebRequest request,
									 @PathVariable UUID seasonId,
									 @RequestParam(name = "markAsWatched") boolean markAsWatched)
	{
		final Optional<Season> seasonOptional = seasonRepository.findById(seasonId);
		if(seasonOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Staffel mit der ID \"{0}\"", seasonId), BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_SHOWS;
		}

		final User user = userService.getCurrentUser();
		final Season season = seasonOptional.get();
		userService.toggleCompleteSeason(user, season, markAsWatched);

		return "redirect:/season/" + season.getId();
	}

	@GetMapping("/episode/{episodeId}/toggle")
	public String toggleEpisodeWatched(WebRequest request, @PathVariable UUID episodeId, Model model)
	{
		final Optional<Episode> episodeOptional = episodeRepository.findById(episodeId);
		if(episodeOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Episode mit der ID \"{0}\"", episodeId), BootstrapColor.DANGER));
			throw new NotFoundException();
		}

		final User user = userService.getCurrentUser();

		final Episode episode = episodeOptional.get();
		if(userService.isWatchedEpisode(user, episode))
		{
			userService.removeWatchedEpisode(user, episode);
		}
		else
		{
			userService.addWatchedEpisode(user, episode);
		}

		final Season season = episode.getSeason();

		model.addAttribute("numberOfElements", userService.getWatchedEpisodesBySeason(user, season).size());
		model.addAttribute("totalNumberOfElements", season.getEpisodes().size());
		return "progressBar";
	}

	@GetMapping("/statistics")
	public String statistics(Model model)
	{
		final User user = userService.getCurrentUser();
		model.addAttribute("currentPage", "Statistiken");
		model.addAttribute("statisticItemsGeneral", userService.getGeneralStatistics(user));
		model.addAttribute("statisticItemsWatchTime", userService.getWatchTimeStatistics(user));

		return "statistics";
	}

	@GetMapping("/movies")
	public String getMovies(WebRequest request, Model model)
	{
		UserNavigationCoordinator.setUserSpecificNavigation(request, true);

		final User user = userService.getCurrentUser();

		String searchText = null;
		if(model.containsAttribute(PARAMETER_NAME_SEARCH_TEXT))
		{
			searchText = (String) model.getAttribute(PARAMETER_NAME_SEARCH_TEXT);
		}

		final List<Movie> movies = movieService.getAllFavoriteByUser(searchText, user);
		final List<Movie> sortedMovies = movies.stream().sorted(Comparator.comparing(m -> m.getName().toLowerCase())).toList();

		model.addAttribute("movies", sortedMovies);

		model.addAttribute("currentPage", "Meine Filme");
		model.addAttribute("userMovies", user.getMovies().stream().map(AddedMovie::getMovie).toList());
		model.addAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, true);

		return ReturnValues.MOVIES;
	}

	@GetMapping("/movies/add/{movieId}")
	@Transactional
	public String addMovie(WebRequest request, @PathVariable UUID movieId)
	{
		final Optional<Movie> movieOptional = movieService.getById(movieId);
		if(movieOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert kein Film mit der ID \"{0}\"", movieId), BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_MOVIES;
		}

		final User user = userService.getCurrentUser();
		user.getMovies().add(new AddedMovie(user, movieOptional.get(), LocalDate.now()));

		return ReturnValues.REDIRECT_MOVIES;
	}

	@GetMapping("/movies/remove/{movieId}")
	public String removeMovie(WebRequest request, @PathVariable UUID movieId)
	{
		final Optional<Movie> movieOptional = movieService.getById(movieId);
		if(movieOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert kein Film mit der ID \"{0}\"", movieId), BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_USER_MOVIES;
		}

		final User user = userService.getCurrentUser();
		final Movie movieToRemove = movieOptional.get();
		final boolean userHadShow = userService.removeMovieFromUser(user, movieToRemove);
		if(!userHadShow)
		{
			WebRequestUtils.putToast(request, new Toast("Der Nutzer hatte den Film nie hinzugefügt.", BootstrapColor.WARNING));
			return ReturnValues.REDIRECT_USER_MOVIES;
		}

		return ReturnValues.REDIRECT_USER_MOVIES;
	}
}
