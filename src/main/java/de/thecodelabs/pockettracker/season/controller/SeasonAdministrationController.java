package de.thecodelabs.pockettracker.season.controller;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.importer.ImportProcessException;
import de.thecodelabs.pockettracker.importer.factory.ImporterNotConfiguredException;
import de.thecodelabs.pockettracker.importer.factory.ShowImporterServiceFactory;
import de.thecodelabs.pockettracker.season.model.EpisodesDialogModel;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.service.SeasonService;
import de.thecodelabs.pockettracker.show.controller.EpisodeInfo;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.UpdateSeasonFromApiDialogModel;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.beans.BeanUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/season")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class SeasonAdministrationController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SeasonAdministrationController.class);

	private final SeasonService seasonService;
	private final UserService userService;
	private final ShowImporterServiceFactory showImporterServiceFactory;

	@Autowired
	public SeasonAdministrationController(SeasonService seasonService, UserService userService, ShowImporterServiceFactory showImporterServiceFactory)
	{
		this.seasonService = seasonService;
		this.userService = userService;
		this.showImporterServiceFactory = showImporterServiceFactory;
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
		if(isSeasonModelInvalid(request, season, validation))
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

		userService.deleteWatchedSeason(season);
		seasonService.deleteSeason(season);
		WebRequestUtils.putToast(request, new Toast("toast.season.delete", BootstrapColor.SUCCESS, seasonName));

		return "redirect:/show/" + showId + "/edit";
	}

	@GetMapping("/{id}/episodesFromApi")
	public String getEpisodesFromApi(@PathVariable Integer id, Model model)
	{
		final Optional<Season> seasonOptional = seasonService.getSeasonById(id);
		if(seasonOptional.isEmpty())
		{
			throw new NotFoundException("Season for id " + id + " not found");
		}

		final Season season = seasonOptional.get();
		final Show show = season.getShow();

		final Map<APIType, List<EpisodeInfo>> episodeInfoByApi = new HashMap<>();
		for(APIIdentifier apiIdentifier : show.getApiIdentifiers())
		{
			try
			{
				final List<EpisodeInfo> seasonInfo = showImporterServiceFactory.getImporter(apiIdentifier.getType()).getAllAvailableEpisodeInfo(Integer.parseInt(apiIdentifier.getIdentifier()), season.getNumber());
				LOGGER.debug(MessageFormat.format("Found {0} episodes for season {1} of show \"{2}\" for api {3}", seasonInfo.size(), season.getNumber(), show.getName(), apiIdentifier.getType()));
				episodeInfoByApi.put(apiIdentifier.getType(), seasonInfo);
			}
			catch(ImportProcessException | IOException | ImporterNotConfiguredException e)
			{
				throw new RuntimeException(e);
			}
		}

		model.addAttribute("season", season);
		model.addAttribute("episodeInfoByApi", episodeInfoByApi);

		return "administration/show/updateSeasonModal";
	}

	@Transactional
	@PostMapping("/{id}/updateFromApi")
	public String updateFromApi(WebRequest request, @PathVariable Integer id,
								@ModelAttribute("formUpdateSeasonFromApi") @Validated UpdateSeasonFromApiDialogModel model)
	{
		final Optional<Season> seasonOptional = seasonService.getSeasonById(id);
		if(seasonOptional.isEmpty())
		{
			throw new NotFoundException("Season for id " + id + " not found");
		}

		final Season season = seasonOptional.get();
		final Show show = season.getShow();

		final Optional<APIIdentifier> apiIdentifierOptional = show.getApiIdentifierByType(model.getApiType());
		if(apiIdentifierOptional.isEmpty())
		{
			throw new IllegalArgumentException("Model does not include api type");
		}

		final APIIdentifier apiIdentifier = apiIdentifierOptional.get();

		try
		{
			showImporterServiceFactory.getImporter(apiIdentifier.getType()).updateSeasonFromApi(Integer.parseInt(apiIdentifier.getIdentifier()), season);
			LOGGER.debug(MessageFormat.format("Updated season {0} of show \"{1}\"", season.getNumber(), show.getName()));
		}
		catch(ImportProcessException | IOException | ImporterNotConfiguredException e)
		{
			throw new RuntimeException(e);
		}

		WebRequestUtils.putToast(request, new Toast("toast.season.updated", BootstrapColor.SUCCESS));

		return "redirect:/show/" + show.getId() + "/edit";
	}

	/*
	Utils
	 */

	private boolean isSeasonModelInvalid(WebRequest request, Season season, BindingResult validation)
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
