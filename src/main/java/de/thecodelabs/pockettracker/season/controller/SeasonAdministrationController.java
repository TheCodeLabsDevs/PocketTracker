package de.thecodelabs.pockettracker.season.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.service.SeasonService;
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

	@Autowired
	public SeasonAdministrationController(SeasonService seasonService)
	{
		this.seasonService = seasonService;
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
