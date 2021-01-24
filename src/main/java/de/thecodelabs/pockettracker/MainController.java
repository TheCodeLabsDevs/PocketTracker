package de.thecodelabs.pockettracker;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.episode.EpisodeRepository;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.Season;
import de.thecodelabs.pockettracker.season.SeasonRepository;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class MainController
{
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

	@GetMapping("/shows")
	public String allShows(Model model)
	{
		model.addAttribute("shows", showRepository.findAllByOrderByNameAsc());

		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final User user = userOptional.get();
		model.addAttribute("currentPage", "Alle Serien");
		model.addAttribute("userShows", user.getShows());
		model.addAttribute("isUserSpecificView", false);

		return "index";
	}

	@GetMapping("/show/{showId}")
	public String show(Model model, @PathVariable Integer showId)
	{
		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			return "redirect:/shows";
		}

		model.addAttribute("show", showOptional.get());
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


	@GetMapping("/login")
	public String login()
	{
		return "login";
	}
}
