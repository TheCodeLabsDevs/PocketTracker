package de.thecodelabs.pockettracker;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.reposiroty.SeasonRepository;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController
{
	public static final String PARAMETER_NAME_IS_USER_SPECIFIC_VIEW = "isUserSpecificView";
	private static final String PARAMETER_NAME_SEARCH_RESULTS = "searchResults";

	private final ShowRepository showRepository;
	private final SeasonRepository seasonRepository;
	private final EpisodeRepository episodeRepository;
	private final UserService userService;

	@Autowired
	public MainController(ShowRepository showRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository, UserService userService)
	{
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

		boolean isUserSpecificView = false;
		if(model.containsAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW))
		{
			isUserSpecificView = (boolean) model.getAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW);
		}

		if(isUserSpecificView)
		{
			model.addAttribute("currentPage", "Meine Serien");
			model.addAttribute("shows", user.getShows());
		}
		else
		{
			model.addAttribute("currentPage", "Alle Serien");
			model.addAttribute("shows", showRepository.findAllByOrderByNameAsc());
		}

		model.addAttribute("userShows", user.getShows());
		model.addAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, isUserSpecificView);

		if(model.containsAttribute(PARAMETER_NAME_SEARCH_RESULTS))
		{
			final List<Show> searchResults = (List<Show>) model.getAttribute(PARAMETER_NAME_SEARCH_RESULTS);
			model.addAttribute("shows", searchResults);
		}

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

		model.addAttribute("show", showOptional.get());
		model.addAttribute("isAdded", user.getShows().contains(showOptional.get()));
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
	@Transactional
	public String search(@RequestParam("searchText") String searchText,
						 @RequestParam(value = "isUserSpecificView", required = false) Boolean isUserSpecificView,
						 RedirectAttributes redirectAttributes)
	{
		if(isUserSpecificView == null)
		{
			isUserSpecificView = false;
		}

		// override lazy fetching
		final List<Show> searchResults = showRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(searchText);
		for(Show show : searchResults)
		{
			for(Season season : show.getSeasons())
			{
				Hibernate.initialize(season.getEpisodes());
			}
		}

		redirectAttributes.addFlashAttribute(PARAMETER_NAME_SEARCH_RESULTS, searchResults);
		redirectAttributes.addFlashAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, isUserSpecificView);
		return "redirect:/shows";
	}
}
