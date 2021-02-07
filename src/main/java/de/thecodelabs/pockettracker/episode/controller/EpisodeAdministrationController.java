package de.thecodelabs.pockettracker.episode.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Controller
@RequestMapping("/episode")
public class EpisodeAdministrationController
{
	private final EpisodeService episodeService;

	@Autowired
	public EpisodeAdministrationController(EpisodeService episodeService)
	{
		this.episodeService = episodeService;
	}

	@GetMapping("/{id}/edit")
	public String getEpisodeView(WebRequest request, @PathVariable Integer id, Model model) {
		final Optional<Episode> episodeOptional = episodeService.getEpisodeById(id);
		if(episodeOptional.isEmpty())
		{
			throw new NotFoundException("Episode with id " + id + " not found");
		}
		final Episode episode = episodeOptional.get();

		final Object oldData = WebRequestUtils.popValidationData(request);
		if(oldData instanceof Season)
		{
			model.addAttribute("episode", oldData);
		}
		else
		{
			model.addAttribute("episode", episode);
		}

		model.addAttribute("season", episode.getSeason());

		return "administration/episode/edit";
	}
}
