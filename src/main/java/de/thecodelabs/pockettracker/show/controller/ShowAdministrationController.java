package de.thecodelabs.pockettracker.show.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import de.thecodelabs.pockettracker.utils.toast.ToastColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Controller
@RequestMapping("/show")
@PreAuthorize("hasAuthority(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class ShowAdministrationController
{
	private final ShowService service;

	@Autowired
	public ShowAdministrationController(ShowService service)
	{
		this.service = service;
	}

	@GetMapping("/{id}/edit")
	public String editPage(@PathVariable Integer id, Model model)
	{
		final Optional<Show> showOptional = service.getShowById(id);
		if(showOptional.isEmpty())
		{
			throw new NotFoundException("Show for id " + id + " not found");
		}

		model.addAttribute("show", showOptional.get());
		return "administration/show/edit";
	}

	@PostMapping("/{id}/edit")
	public String editPost(WebRequest request, @PathVariable Integer id, @Validated @ModelAttribute("show") Show show, BindingResult validation, Model model)
	{
		if(validation.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("Validierungfehler", ToastColor.DANGER));
			WebRequestUtils.putValidationError(request, validation);
			return "redirect:/show/" + id + "/edit";
		}
		return "redirect:/show/" + id;
	}
}
