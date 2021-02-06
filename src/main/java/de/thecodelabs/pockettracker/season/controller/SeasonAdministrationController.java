package de.thecodelabs.pockettracker.season.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/season")
public class SeasonAdministrationController
{
	private final SeasonService seasonService;

	@Autowired
	public SeasonAdministrationController(SeasonService seasonService)
	{
		this.seasonService = seasonService;
	}

	@GetMapping("/{id}/edit")
	public String seasonEditView(@PathVariable Integer id, Model model)
	{
		final Optional<Season> seasonOptional = seasonService.getSeasonById(id);
		if(seasonOptional.isEmpty())
		{
			throw new NotFoundException("Season with id " + id + " not found");
		}

		model.addAttribute("season", seasonOptional.get());
		return "administration/season/edit";
	}
}
