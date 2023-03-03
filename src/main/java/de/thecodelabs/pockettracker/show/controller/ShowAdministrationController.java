package de.thecodelabs.pockettracker.show.controller;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.apiidentifier.APIIdentifierService;
import de.thecodelabs.pockettracker.exceptions.InternalServerException;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.importer.ImportProcessException;
import de.thecodelabs.pockettracker.importer.factory.ImporteNotConfiguredException;
import de.thecodelabs.pockettracker.importer.factory.ShowImporterServiceFactory;
import de.thecodelabs.pockettracker.importer.model.ShowSearchItem;
import de.thecodelabs.pockettracker.importer.model.ShowSearchRequest;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.show.model.SeasonsDialogModel;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowImageType;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/show")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class ShowAdministrationController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowAdministrationController.class);

	private final UserService userService;
	private final ShowService service;
	private final APIIdentifierService apiIdentifierService;

	private final ShowImporterServiceFactory showImporterServiceFactory;

	@Autowired
	public ShowAdministrationController(UserService userService, ShowService service, APIIdentifierService apiIdentifierService, ShowImporterServiceFactory showImporterServiceFactory)
	{
		this.userService = userService;
		this.service = service;
		this.apiIdentifierService = apiIdentifierService;
		this.showImporterServiceFactory = showImporterServiceFactory;
	}

	@GetMapping("/create")
	public String createPage(WebRequest request, Model model)
	{
		final Object oldData = WebRequestUtils.popValidationData(request);
		if(oldData instanceof Show)
		{
			model.addAttribute("show", oldData);
		}
		else
		{
			model.addAttribute("show", new Show());
		}
		model.addAttribute("back_url", "/shows");

		return "administration/show/edit";
	}

	@PostMapping("/create")
	@Transactional
	public String createPost(WebRequest request, @Validated @ModelAttribute("show") Show show, BindingResult validation)
	{
		if(isModelInvalid(request, show, validation))
		{
			return "redirect:/show/create";
		}

		final Show createdShow = service.createShow(show);

		return "redirect:/show/" + createdShow.getId() + "/edit";
	}

	@PostMapping("/searchApi")
	public String searchShowInApiPost(@ModelAttribute ShowSearchRequest searchRequest, Model model)
	{
		try
		{
			final List<ShowSearchItem> items = showImporterServiceFactory.getImporter(searchRequest.getType())
					.searchForShow(searchRequest.getSearch());
			model.addAttribute("items", items);
			model.addAttribute("type", searchRequest.getType());
			return "administration/show/api/searchResult";
		}
		catch(ImporteNotConfiguredException | IOException e)
		{
			throw new InternalServerException("Cannot execute api search", e);
		}
		catch(ImportProcessException e)
		{
			throw new InternalServerException("Invalid search response", e);
		}
	}

	@PostMapping("/createFromApi")
	@Transactional
	public String createFromApiPost(WebRequest request, @ModelAttribute("newApiIdentifier") @Validated APIIdentifier apiIdentifier, BindingResult validation)
	{
		if(isModelInvalid(request, apiIdentifier, validation))
		{
			return "redirect:/shows";
		}

		try
		{
			final Show importedShow = showImporterServiceFactory.getImporter(apiIdentifier.getType()).createShow(apiIdentifier.getIdentifier());
			final Show createdShow = service.createShow(importedShow);

			return "redirect:/show/" + createdShow.getId() + "/edit";
		}
		catch(ImporteNotConfiguredException | IOException e)
		{
			throw new InternalServerException("Cannot import show", e);
		}
		catch(ImportProcessException e)
		{
			throw new InternalServerException("Display error in UI", e); // TODO: Show error in ui
		}
	}

	@GetMapping("/{id}/edit")
	public String editPage(WebRequest request, @PathVariable Integer id, Model model)
	{
		final Optional<Show> showOptional = service.getShowById(id);
		if(showOptional.isEmpty())
		{
			throw new NotFoundException("Show for id " + id + " not found");
		}

		final Object oldData = WebRequestUtils.popValidationData(request);
		final Show show;
		if(oldData instanceof Show oldShow)
		{
			show = oldShow;
		}
		else
		{
			show = showOptional.get();
		}
		model.addAttribute("show", show);

		model.addAttribute("back_url", "/show/" + id);

		return "administration/show/edit";
	}

	@PostMapping("/{id}/edit")
	@Transactional
	public String editPost(WebRequest request, @PathVariable Integer id, @Validated @ModelAttribute("show") Show show, BindingResult validation)
	{
		if(isModelInvalid(request, show, validation))
		{
			return "redirect:/show/" + id + "/edit";
		}

		final Optional<Show> managedShowOptional = service.getShowById(id);
		if(managedShowOptional.isEmpty())
		{
			throw new NotFoundException("Show for id " + id + " not found");
		}
		final Show managedShow = managedShowOptional.get();
		BeanUtils.merge(show, managedShow);

		return "redirect:/show/" + id;
	}

	@PostMapping("/{id}/edit/{type}")
	public String updateImage(WebRequest request, @PathVariable Integer id, @PathVariable ShowImageType type,
							  @RequestParam("image") MultipartFile multipartFile)
	{
		Optional<Show> showOptional = service.getShowById(id);
		if(showOptional.isEmpty())
		{
			return "redirect:/show/" + id + "/edit";
		}
		final Show show = showOptional.get();

		if(multipartFile == null || multipartFile.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.null", BootstrapColor.WARNING));
			service.deleteShowImage(type, show);
			return "redirect:/show/" + id + "/edit";
		}

		try
		{
			service.changeShowImage(type, show, Optional.ofNullable(multipartFile.getOriginalFilename()).orElse(show.getName()), multipartFile.getInputStream());
			WebRequestUtils.putToast(request, new Toast("toast.image.saved", BootstrapColor.SUCCESS));
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.error", BootstrapColor.WARNING));
			LOGGER.error("Fail to change banner image", e);
		}
		return "redirect:/show/" + id + "/edit";
	}

	@PostMapping("/{id}/season/add")
	public String addSeasons(WebRequest request, @PathVariable Integer id,
							 @ModelAttribute("addSeason") @Validated SeasonsDialogModel model, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, model);
			return "redirect:/show/" + id + "/edit";
		}

		Optional<Season> firstSeason = Optional.empty();
		for(int index = 0; index < model.getSeasonCount(); index++)
		{
			final Season season = service.addSeasonToShow(id);
			if(firstSeason.isEmpty())
			{
				firstSeason = Optional.of(season);
			}
		}
		return firstSeason.map(season -> "redirect:/season/" + season.getId() + "/edit")
				.orElseGet(() -> "redirect:/show/" + id + "/edit");
	}

	@PostMapping("/{id}/delete")
	@Transactional
	public String deletePost(WebRequest request, @PathVariable Integer id)
	{
		final Optional<Show> managedShowOptional = service.getShowById(id);
		if(managedShowOptional.isEmpty())
		{
			throw new NotFoundException("Show for id " + id + " not found");
		}
		final Show managedShow = managedShowOptional.get();
		final String showName = managedShow.getName();

		userService.deleteWatchedShow(managedShow);
		service.deleteShow(managedShow);

		WebRequestUtils.putToast(request, new Toast("toast.show.delete", BootstrapColor.SUCCESS, showName));
		return "redirect:/shows";
	}

	@PostMapping("/{showId}/apiIdentifier/add")
	public String addApiIdentifier(WebRequest request, @PathVariable Integer showId,
								   @ModelAttribute("newApiIdentifier") @Validated APIIdentifier apiIdentifier, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, apiIdentifier);
			return "redirect:/show/" + showId + "/edit";
		}

		try
		{
			service.addApiIdentifierToShow(showId, apiIdentifier);
		}
		catch(IllegalArgumentException e)
		{
			validation.addError(new FieldError("newApiIdentifier", "type", "", false, new String[]{"show.apiIdentifiers.warning.already.exists"}, new Object[]{apiIdentifier.getType()}, null));
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, apiIdentifier);
		}

		return "redirect:/show/" + showId + "/edit";
	}

	@PostMapping("/{showId}/apiIdentifier/delete/{id}")
	public String deleteApiIdentifier(WebRequest request, @PathVariable Integer showId, @PathVariable Integer id)
	{
		final Optional<APIIdentifier> identifierOptional = apiIdentifierService.getIdentifierById(id);
		if(identifierOptional.isEmpty())
		{
			throw new NotFoundException("APIIdentifier for id " + id + " not found");
		}

		final APIIdentifier apiIdentifier = identifierOptional.get();
		apiIdentifierService.deleteIdentifier(apiIdentifier);

		WebRequestUtils.putToast(request, new Toast("toast.api.identifier.delete", BootstrapColor.SUCCESS, apiIdentifier.getType()));

		return "redirect:/show/" + showId + "/edit";
	}

	@GetMapping("/{id}/showImages/{type}")
	public String getShowImages(@PathVariable Integer id, @PathVariable ShowImageType type, Model model)
	{
		final Optional<Show> showOptional = service.getShowById(id);
		if(showOptional.isEmpty())
		{
			throw new NotFoundException("Show for id " + id + " not found");
		}

		final Show show = showOptional.get();

		final Map<APIType, List<String>> urlsByApi = new HashMap<>();
		for(APIIdentifier apiIdentifier : show.getApiIdentifiers())
		{
			final List<String> posterUrls;
			try
			{
				if(type == ShowImageType.POSTER)
				{
					posterUrls = showImporterServiceFactory.getImporter(apiIdentifier.getType()).getShowPosterImageUrls(Integer.parseInt(apiIdentifier.getIdentifier()));
				}
				else
				{
					posterUrls = showImporterServiceFactory.getImporter(apiIdentifier.getType()).getShowBannerImageUrls(Integer.parseInt(apiIdentifier.getIdentifier()));
				}
				LOGGER.debug(MessageFormat.format("Found {0} image urls", posterUrls.size()));
				urlsByApi.put(apiIdentifier.getType(), posterUrls);
			}
			catch(ImportProcessException | IOException | ImporteNotConfiguredException e)
			{
				throw new RuntimeException(e);
			}
		}

		model.addAttribute("show", show);
		model.addAttribute("urlsByApi", urlsByApi);

		return MessageFormat.format("administration/show/{0}ImagesModal", type.name().toLowerCase());
	}

	@PostMapping("/{showId}/edit/imageFromApi/{type}")
	public String addImageFromApi(WebRequest request, @PathVariable Integer showId, @PathVariable ShowImageType type, @RequestParam String url)
	{
		final Optional<Show> showOptional = service.getShowById(showId);
		if(showOptional.isEmpty())
		{
			return "redirect:/show/" + showId + "/edit";
		}

		final Show show = showOptional.get();

		service.deleteShowImage(type, show);

		try(final InputStream dataStream = new URL(url).openStream())
		{
			service.changeShowImage(type, show, show.getName(), dataStream);
			WebRequestUtils.putToast(request, new Toast("toast.image.saved", BootstrapColor.SUCCESS));
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.image.error", BootstrapColor.WARNING));
			LOGGER.error("Fail to change show image", e);
		}
		return "redirect:/show/" + showId + "/edit";
	}

	/*
	 Utils
	 */

	private <T> boolean isModelInvalid(WebRequest request, T show, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, show);
			return true;
		}
		return false;
	}
}
