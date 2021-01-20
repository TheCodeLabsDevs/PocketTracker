package de.thecodelabs.pockettracker;

import de.thecodelabs.pockettracker.episode.EpisodeRepository;
import de.thecodelabs.pockettracker.season.SeasonRepository;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowRepository;
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


	@Autowired
	public MainController(ShowRepository showRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository)
	{
		this.showRepository = showRepository;
		this.seasonRepository = seasonRepository;
		this.episodeRepository = episodeRepository;
	}

	@GetMapping
	public String allShows(Model model)
	{
		model.addAttribute("shows", showRepository.findAllByOrderByNameAsc());
		return "index";
	}

	@GetMapping("/show/{showId}")
	public String show(Model model, @PathVariable Integer showId)
	{
		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			return "redirect:/";
		}

		model.addAttribute("show", showOptional.get());
		return "showDetails";
	}

	@GetMapping("/login")
	public String login()
	{
		return "login";
	}

	@GetMapping("/episodes")
	public String episodes()
	{
		return "episodes";
	}
}
