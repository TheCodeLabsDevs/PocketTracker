package de.thecodelabs.pockettracker.season.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.EpisodesDialogModel;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.service.SeasonService;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Controller
@RequestMapping("/season")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class SeasonAdministrationController
{
	private final SeasonService seasonService;
	private final UserService userService;

	@Autowired
	public SeasonAdministrationController(SeasonService seasonService, UserService userService)
	{
		this.seasonService = seasonService;
		this.userService = userService;
	}

	@GetMapping("/{id}/edit")
	public String seasonEditView(WebRequest request, @PathVariable Integer id, Model model)
	{
		final Optional<Season> seasonOptional = seasonService.getSeasonById(id);
		if(seasonOptional.isEmpty())
		{
			throw new NotFoundException("Season with id " + id + " not found");
		}
		final Season season = seasonOptional.get();

		final Object oldData = WebRequestUtils.popValidationData(request);
		if(oldData instanceof Season)
		{
			model.addAttribute("season", oldData);
		}
		else
		{
			model.addAttribute("season", season);
		}

		model.addAttribute("show", season.getShow());
		model.addAttribute("episodes", season.getEpisodes());

		return "administration/season/edit";
	}

	@PostMapping("/{id}/edit")
	@Transactional
	public String seasonEditSubmit(WebRequest request, @PathVariable Integer id,
								   @ModelAttribute("season") @Validated Season season, BindingResult validation)
	{
		if(isSeasonModelInvalide(request, season, validation))
		{
			return "redirect:/season/" + id + "/edit";
		}

		final Optional<Season> managedSeasonOptional = seasonService.getSeasonById(id);
		if(managedSeasonOptional.isEmpty())
		{
			throw new NotFoundException("Season for id " + id + " not found");
		}
		final Season managedSeason = managedSeasonOptional.get();
		BeanUtils.merge(season, managedSeason);

		return "redirect:/season/" + id + "/edit";
	}

	@PostMapping("/{id}/episode/add")
	public String addEpisode(WebRequest request, @PathVariable Integer id,
							 @ModelAttribute("addEpisode") @Validated EpisodesDialogModel model, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, model);
			return "redirect:/season/" + id + "/edit";
		}

		Optional<Episode> firstEpisode = Optional.empty();
		for(int index = 0; index < model.getEpisodeCount(); index++)
		{
			final Episode episode = seasonService.addEpisodeToSeason(id);
			if(firstEpisode.isEmpty())
			{
				firstEpisode = Optional.of(episode);
			}
		}
		return firstEpisode.map(episode -> "redirect:/episode/" + episode.getId() + "/edit")
				.orElseGet(() -> "redirect:/season/" + id + "/edit");
	}

	@PostMapping("/{id}/delete")
	public String deleteSeasonSubmit(WebRequest request, @PathVariable Integer id)
	{
		final Optional<Season> seasonOptional = seasonService.getSeasonById(id);
		if(seasonOptional.isEmpty())
		{
			throw new NotFoundException("Season for id " + id + " not found");
		}

		final Season season = seasonOptional.get();
		final Integer showId = season.getShow().getId();
		final String seasonName = season.getName();

		userService.deleteSeason(season);
		seasonService.deleteSeason(season);
		WebRequestUtils.putToast(request, new Toast("toast.season.delete", BootstrapColor.SUCCESS, seasonName));

		return "redirect:/show/" + showId + "/edit";
	}


	/*
	Utils
	 */

	private boolean isSeasonModelInvalide(WebRequest request, Season season, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, season);
			return true;
		}
		return false;
	}
}
