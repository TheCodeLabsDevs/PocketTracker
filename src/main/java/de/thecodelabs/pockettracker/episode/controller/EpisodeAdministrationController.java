package de.thecodelabs.pockettracker.episode.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
	public String getEpisodeView(WebRequest request, @PathVariable Integer id, Model model)
	{
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

	@PostMapping("/{id}/edit")
	@Transactional
	public String episodeEditSubmit(WebRequest request, @PathVariable Integer id,
									@ModelAttribute("episode") @Validated Episode episode, BindingResult validation)
	{
		if(isEpisodeModelInvalide(request, episode, validation))
		{
			return "redirect:/episode/" + id + "/edit";
		}

		final Optional<Episode> managedEpisodeOptional = episodeService.getEpisodeById(id);
		if(managedEpisodeOptional.isEmpty())
		{
			throw new NotFoundException("Episode for id " + id + " not found");
		}
		final Episode managedEpisode = managedEpisodeOptional.get();
		BeanUtils.merge(episode, managedEpisode);

		return "redirect:/episode/" + id + "/edit";
	}


	/*
	Utils
	 */

	private boolean isEpisodeModelInvalide(WebRequest request, Episode episode, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, episode);
			return true;
		}
		return false;
	}
}