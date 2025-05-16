package de.thecodelabs.pockettracker.episode.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.model.EpisodeImageType;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/episode")
@RequiredArgsConstructor
@Slf4j
public class EpisodeAdministrationController
{
	private final EpisodeService episodeService;
	private final UserService userService;

	@GetMapping("/{id}/edit")
	public String getEpisodeView(WebRequest request, @PathVariable UUID id, Model model)
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

		model.addAttribute("previousEpisode", episodeService.getPreviousEpisode(episode).orElse(null));
		model.addAttribute("nextEpisode", episodeService.getNextEpisode(episode).orElse(null));

		return "administration/episode/edit";
	}

	@PostMapping("/{id}/edit")
	@Transactional
	public String episodeEditSubmit(WebRequest request, @PathVariable UUID id,
									@ModelAttribute("episode") @Validated Episode episode, BindingResult validation)
	{
		if(isEpisodeModelInvalid(request, episode, validation))
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

	@PostMapping("/{id}/delete")
	public String deleteEpisodeSubmit(WebRequest request, @PathVariable UUID id)
	{
		final Optional<Episode> episodeOptional = episodeService.getEpisodeById(id);
		if(episodeOptional.isEmpty())
		{
			throw new NotFoundException("Episode with id " + id + " not found");
		}

		final Episode episode = episodeOptional.get();
		final Season season = episode.getSeason();
		final String episodeName = episode.getName();

		userService.deleteWatchedEpisodes(episode);
		episodeService.deleteEpisode(episode);

		WebRequestUtils.putToast(request, new Toast("toast.episode.delete", BootstrapColor.SUCCESS, episodeName));

		return "redirect:/season/" + season.getId() + "/edit";
	}

	@PostMapping("/{id}/edit/{type}")
	public String updateImage(WebRequest request, @PathVariable UUID id, @PathVariable EpisodeImageType type,
							  @RequestParam("image") MultipartFile multipartFile)
	{
		Optional<Episode> episodeOptional = episodeService.getEpisodeById(id);
		if(episodeOptional.isEmpty())
		{
			return "redirect:/episode/" + id + "/edit";
		}
		final Episode episode = episodeOptional.get();

		if(multipartFile == null || multipartFile.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.null", BootstrapColor.WARNING));
			episodeService.deleteEpisodeImage(type, episode);
			return "redirect:/episode/" + id + "/edit";
		}

		try
		{
			episodeService.changeEpisodeImage(type, episode, multipartFile);
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.error", BootstrapColor.WARNING));
			log.error("Fail to change image", e);
		}
		return "redirect:/episode/" + id + "/edit";
	}

	/*
	Utils
	 */

	private boolean isEpisodeModelInvalid(WebRequest request, Episode episode, BindingResult validation)
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
