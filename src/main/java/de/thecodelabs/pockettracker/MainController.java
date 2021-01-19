package de.thecodelabs.pockettracker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController
{
	@GetMapping
	public String index()
	{
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
