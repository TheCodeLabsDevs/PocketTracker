package de.thecodelabs.pockettracker;

import de.thecodelabs.pockettracker.show.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController
{
	private final ShowRepository showRepository;

	@Autowired
	public MainController(ShowRepository showRepository)
	{
		this.showRepository = showRepository;
	}

	@GetMapping
	public String index(Model model)
	{
		model.addAttribute("shows", showRepository.findAll());
		return "index";
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
