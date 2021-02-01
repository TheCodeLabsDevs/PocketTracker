package de.thecodelabs.pockettracker.show.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowImageType;
import de.thecodelabs.pockettracker.show.ShowService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/show")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class ShowAdministrationController
{
	private static final Logger logger = LoggerFactory.getLogger(ShowAdministrationController.class);

	private final ShowService service;

	@Autowired
	public ShowAdministrationController(ShowService service)
	{
		this.service = service;
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
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, show);
			return "redirect:/show/create";
		}

		final Show createdShow = service.createShow(show);

		return "redirect:/show/" + createdShow.getId() + "/edit";
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
		if(oldData instanceof Show)
		{
			model.addAttribute("show", oldData);
		}
		else
		{
			model.addAttribute("show", showOptional.get());
		}
		model.addAttribute("back_url", "/show/" + id);

		return "administration/show/edit";
	}

	@PostMapping("/{id}/edit")
	@Transactional
	public String editPost(WebRequest request, @PathVariable Integer id, @Validated @ModelAttribute("show") Show show, BindingResult validation)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("toast.validation", BootstrapColor.DANGER));
			WebRequestUtils.putValidationError(request, validation, show);
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
	public String updateBanner(WebRequest request, @PathVariable Integer id, @PathVariable ShowImageType type,
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
			WebRequestUtils.putToast(request, new Toast("toast.show.image.null", BootstrapColor.WARNING));
			service.deleteShowImage(type, show);
			return "redirect:/show/" + id + "/edit";
		}

		try
		{
			service.changeShowImage(type, show, multipartFile);
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.show.image.null", BootstrapColor.WARNING));
			logger.error("Fail to change banner image", e);
		}
		return "redirect:/show/" + id + "/edit";
	}
}
