package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.movie.MovieService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.season.SeasonRepository;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserSettings;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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
		public static final String REDIRECT_SHOWS = "redirect:/user/shows";
		public static final String REDIRECT_MOVIES = "redirect:/user/movies";
		public static final String MOVIES = "movies";
	}

	@PostMapping("/shows")
	@Transactional
	public String postFilterSubmission(@RequestParam ShowSortOption sortOption,
									   @RequestParam ShowFilterOption filterOption,
									   @RequestParam Boolean showHiddenShows)
	{
		final UserSettings settings = userService.getUserSettings();
		settings.setLastShowSortOption(sortOption);
		settings.setLastShowFilterOption(filterOption);
		settings.setShowHiddenShows(showHiddenShows);

		return ReturnValues.REDIRECT_SHOWS;
	}

	@GetMapping("/shows")
	public String getShows(Model model)
	{
		userService.prepareShowsModel(model, null);
		return ReturnValues.INDEX;
	}

	@GetMapping("/shows/toggleShowHidden/{showId}")
	@Transactional
	public String toggleShowHidden(WebRequest request, @PathVariable UUID showId)
	{
		final Optional<Show> showOptional = showService.getById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_SHOWS;
		}

		userService.toggleShowHidden(showOptional.get());

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
	public String getMovies(Model model)
	{
		userService.prepareMoviesModel(model, null);
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
			return ReturnValues.REDIRECT_MOVIES;
		}

		final User user = userService.getCurrentUser();
		final Movie movieToRemove = movieOptional.get();
		final boolean userHadShow = userService.removeMovieFromUser(user, movieToRemove);
		if(!userHadShow)
		{
			WebRequestUtils.putToast(request, new Toast("Der Nutzer hatte den Film nie hinzugefügt.", BootstrapColor.WARNING));
			return ReturnValues.REDIRECT_MOVIES;
		}

		return ReturnValues.REDIRECT_MOVIES;
	}
}
