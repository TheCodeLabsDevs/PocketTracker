package de.thecodelabs.pockettracker;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.movie.MovieService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.reposiroty.SeasonRepository;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.navigation.UserNavigationCoordinator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MainController
{
	public static final String PARAMETER_NAME_IS_USER_SPECIFIC_VIEW = "isUserSpecificView";
	public static final String PARAMETER_NAME_SEARCH_TEXT = "searchText";

	private final ShowService showService;
	private final ShowRepository showRepository;
	private final SeasonRepository seasonRepository;
	private final EpisodeRepository episodeRepository;
	private final UserService userService;
	private final MovieService movieService;

	private static class ModelAttributes
	{
		public static final String NUMBER_OF_ALL_SHOWS = "numberOfAllShows";
		public static final String SHOWS = "shows";
		public static final String USER_SHOWS = "userShows";
		public static final String CURRENT_PAGE = "currentPage";
		public static final String SHOW = "show";
		public static final String IS_ADDED = "isAdded";
		public static final String IS_DISLIKED = "isDisliked";
		public static final String LATEST_WATCHED = "latestWatched";
		public static final String SEASON = "season";
		public static final String EPISODE = "episode";
		public static final String NUMBER_OF_ALL_MOVIES = "numberOfAllMovies";
		public static final String MOVIES = "movies";
		public static final String USER_MOVIES = "userMovies";
		public static final String MOVIE = "movie";
	}

	private static class ReturnValues
	{
		public static final String INDEX = "index";
		public static final String REDIRECT_SHOWS = "redirect:/shows";
		public static final String REDIRECT_USER_SHOWS = "redirect:/user/shows";
		public static final String SHOW = "show";
		public static final String SEASON = "season";
		public static final String EPISODE = "episode";
		public static final String MOVIES = "movies";
		public static final String MOVIE = "movie";
		public static final String REDIRECT_MOVIES = "redirect:/movies";
	}

	@Autowired
	public MainController(ShowService showService, ShowRepository showRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository, UserService userService, MovieService movieService)
	{
		this.showService = showService;
		this.showRepository = showRepository;
		this.seasonRepository = seasonRepository;
		this.episodeRepository = episodeRepository;
		this.userService = userService;
		this.movieService = movieService;
	}

	@SuppressWarnings("squid:S1319")
	@GetMapping("/shows")
	public String allShows(WebRequest request, Model model)
	{
		UserNavigationCoordinator.setUserSpecificNavigation(request, false);

		final User user = userService.getCurrentUser();

		String searchText = null;
		if(model.containsAttribute(PARAMETER_NAME_SEARCH_TEXT))
		{
			searchText = (String) model.getAttribute(PARAMETER_NAME_SEARCH_TEXT);
		}

		model.addAttribute(ModelAttributes.NUMBER_OF_ALL_SHOWS, showService.getAllShows(null).size());
		model.addAttribute(ModelAttributes.SHOWS, showService.getAllShows(searchText));

		model.addAttribute(ModelAttributes.CURRENT_PAGE, "Alle Serien");
		model.addAttribute(ModelAttributes.USER_SHOWS, user.getShows().stream().map(AddedShow::getShow).toList());
		model.addAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, false);

		return ReturnValues.INDEX;
	}

	@GetMapping("/show/{showId}")
	public String show(Model model, @PathVariable UUID showId)
	{
		final User user = userService.getCurrentUser();
		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_SHOWS;
		}

		final Show show = showOptional.get();
		final LocalDate latestWatchDate = ShowSortOption.getLatestWatchDate(show, user);

		model.addAttribute(ModelAttributes.SHOW, show);
		model.addAttribute(ModelAttributes.IS_ADDED, user.getShowById(show.getId()).isPresent());
		model.addAttribute(ModelAttributes.IS_DISLIKED, user.getShowById(show.getId()).map(AddedShow::getDisliked).orElse(false));

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
	public String search(@RequestParam("searchText") String searchText,
						 @RequestParam(value = "isUserSpecificView", required = false) Boolean isUserSpecificView,
						 RedirectAttributes redirectAttributes)
	{
		if(isUserSpecificView == null)
		{
			isUserSpecificView = false;
		}

		redirectAttributes.addFlashAttribute(PARAMETER_NAME_SEARCH_TEXT, searchText);

		if(isUserSpecificView)
		{
			return ReturnValues.REDIRECT_USER_SHOWS;
		}
		else
		{
			return ReturnValues.REDIRECT_SHOWS;
		}
	}

	@SuppressWarnings("squid:S1319")
	@GetMapping("/movies")
	public String allMovies(WebRequest request, Model model)
	{
		UserNavigationCoordinator.setUserSpecificNavigation(request, false);

		final User user = userService.getCurrentUser();

		String searchText = null;
		if(model.containsAttribute(PARAMETER_NAME_SEARCH_TEXT))
		{
			searchText = (String) model.getAttribute(PARAMETER_NAME_SEARCH_TEXT);
		}

		model.addAttribute(ModelAttributes.NUMBER_OF_ALL_MOVIES, movieService.getAllMovies(null).size());
		model.addAttribute(ModelAttributes.MOVIES, movieService.getAllMovies(searchText));

		model.addAttribute(ModelAttributes.CURRENT_PAGE, "Alle Serien");
		model.addAttribute(ModelAttributes.USER_MOVIES, user.getMovies().stream().map(AddedMovie::getMovie).toList());
		model.addAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, false);

		return ReturnValues.MOVIES;
	}

	@GetMapping("/movie/{movieId}")
	public String movie(Model model, @PathVariable UUID movieId)
	{
		final User user = userService.getCurrentUser();
		final Optional<Movie> movieOptional = movieService.getMovieById(movieId);
		if(movieOptional.isEmpty())
		{
			return ReturnValues.REDIRECT_MOVIES;
		}

		final Movie movie = movieOptional.get();
		// TODO
//		final LocalDate latestWatchDate = ShowSortOption.getLatestWatchDate(movie, user);

		model.addAttribute(ModelAttributes.MOVIE, movie);
		model.addAttribute(ModelAttributes.IS_ADDED, user.getShowById(movie.getId()).isPresent());

//		if(latestWatchDate != null && latestWatchDate != LocalDate.MIN)
//		{
//			model.addAttribute(ModelAttributes.LATEST_WATCHED, latestWatchDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
//		}

		return ReturnValues.MOVIE;
	}
}
