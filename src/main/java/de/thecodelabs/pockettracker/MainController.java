package de.thecodelabs.pockettracker;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.reposiroty.SeasonRepository;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

	@Autowired
	public MainController(ShowService showService, ShowRepository showRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository, UserService userService)
	{
		this.showService = showService;
		this.showRepository = showRepository;
		this.seasonRepository = seasonRepository;
		this.episodeRepository = episodeRepository;
		this.userService = userService;
	}

	@SuppressWarnings("squid:S1319")
	@GetMapping("/shows")
	public String allShows(Model model)
	{
		final User user = userService.getCurrentUser();

		String searchText = null;
		if(model.containsAttribute(PARAMETER_NAME_SEARCH_TEXT))
		{
			searchText = (String) model.getAttribute(PARAMETER_NAME_SEARCH_TEXT);
		}

		model.addAttribute("numberOfAllShows", showService.getAllShows(null).size());
		model.addAttribute("shows", showService.getAllShows(searchText));

		model.addAttribute("currentPage", "Alle Serien");
		model.addAttribute("userShows", user.getShows());
		model.addAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, false);

		return "index";
	}

	@GetMapping("/show/{showId}")
	public String show(Model model, @PathVariable Integer showId)
	{
		final User user = userService.getCurrentUser();
		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			return "redirect:/shows";
		}

		final Show show = showOptional.get();
		final LocalDate latestWatchDate = ShowSortOption.getLatestWatchDate(show, user);

		model.addAttribute("show", show);
		model.addAttribute("isAdded", user.getShowById(show.getId()).isPresent());
		model.addAttribute("isDisliked", user.getShowById(show.getId()).map(AddedShow::getDisliked).orElse(false));

		if(latestWatchDate != null && latestWatchDate != LocalDate.MIN)
		{
			model.addAttribute("latestWatched", latestWatchDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		}

		return "show";
	}

	@GetMapping("/season/{seasonId}")
	public String season(Model model, @PathVariable Integer seasonId)
	{
		final Optional<Season> seasonOptional = seasonRepository.findById(seasonId);
		if(seasonOptional.isEmpty())
		{
			return "redirect:/shows";
		}

		model.addAttribute("season", seasonOptional.get());
		return "season";
	}

	@GetMapping("/episode/{episodeId}")
	public String episode(Model model, @PathVariable Integer episodeId)
	{
		final Optional<Episode> episodeOptional = episodeRepository.findById(episodeId);
		if(episodeOptional.isEmpty())
		{
			return "redirect:/shows";
		}

		model.addAttribute("episode", episodeOptional.get());
		return "episode";
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
			return "redirect:/user/shows";
		}
		else
		{
			return "redirect:/shows";
		}
	}
}
