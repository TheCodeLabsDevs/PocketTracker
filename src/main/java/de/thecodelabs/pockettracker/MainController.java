package de.thecodelabs.pockettracker;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.movie.MovieService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.season.SeasonRepository;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MainController
{
	private final ShowService showService;
	private final SeasonRepository seasonRepository;
	private final EpisodeRepository episodeRepository;
	private final UserService userService;
	private final MovieService movieService;

	private static class ModelAttributes
	{
		public static final String SHOW = "show";
		public static final String IS_ADDED = "isAdded";
		public static final String IS_HIDDEN = "isHidden";
		public static final String LATEST_WATCHED = "latestWatched";
		public static final String SEASON = "season";
		public static final String EPISODE = "episode";
		public static final String MOVIE = "movie";
	}

	private static class ReturnValues
	{
		public static final String REDIRECT_SHOWS = "redirect:/user/shows";
		public static final String SHOW = "show";
		public static final String SEASON = "season";
		public static final String EPISODE = "episode";
		public static final String MOVIE = "movie";
		public static final String REDIRECT_MOVIES = "redirect:/user/movies";
		public static final String SEARCH = "search";
	}

	@GetMapping("/show/{showId}")
	public String show(Model model, @PathVariable UUID showId)
	{
		final User user = userService.getCurrentUser();
		final Optional<Show> showOptional = showService.getById(showId);
		if(showOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_SHOWS;
		}

		final Show show = showOptional.get();
		final LocalDate latestWatchDate = ShowSortOption.getLatestWatchDate(show, user);

		model.addAttribute(ModelAttributes.SHOW, show);
		model.addAttribute(ModelAttributes.IS_HIDDEN, user.isShowHidden(show.getId()));

		if(latestWatchDate != null && latestWatchDate != LocalDate.MIN)
		{
			model.addAttribute(ModelAttributes.LATEST_WATCHED, latestWatchDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		}

		return ReturnValues.SHOW;
	}

	@GetMapping("/season/{seasonId}")
	public String season(Model model, @PathVariable UUID seasonId)
	{
		final Optional<Season> seasonOptional = seasonRepository.findById(seasonId);
		if(seasonOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_SHOWS;
		}

		model.addAttribute(ModelAttributes.SEASON, seasonOptional.get());
		return ReturnValues.SEASON;
	}

	@GetMapping("/episode/{episodeId}")
	public String episode(Model model, @PathVariable UUID episodeId)
	{
		final Optional<Episode> episodeOptional = episodeRepository.findById(episodeId);
		if(episodeOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_SHOWS;
		}

		model.addAttribute(ModelAttributes.EPISODE, episodeOptional.get());
		return ReturnValues.EPISODE;
	}

	@PostMapping("/search")
	public String search(@RequestParam("searchText") String searchText,  Model model)
	{
		userService.prepareShowsModel(model, searchText);
		userService.prepareMoviesModel(model, searchText);

		model.addAttribute("currentPage", "Suche");
		return ReturnValues.SEARCH;
	}

	@GetMapping("/movie/{movieId}")
	public String movie(Model model, @PathVariable UUID movieId)
	{
		final User user = userService.getCurrentUser();
		final Optional<Movie> movieOptional = movieService.getById(movieId);
		if(movieOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_MOVIES;
		}

		final Movie movie = movieOptional.get();

		model.addAttribute(ModelAttributes.MOVIE, movie);
		model.addAttribute(ModelAttributes.IS_ADDED, user.getMovieById(movie.getId()).isPresent());

		return ReturnValues.MOVIE;
	}
}
