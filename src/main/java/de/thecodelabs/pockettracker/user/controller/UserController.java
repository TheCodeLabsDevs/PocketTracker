package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.User;
import de.thecodelabs.pockettracker.user.UserService;
import de.thecodelabs.pockettracker.utils.Toast;
import de.thecodelabs.pockettracker.utils.ToastColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController
{
	private final UserService userService;

	@Autowired
	public UserController(UserService userService)
	{
		this.userService = userService;
	}

	@GetMapping
	public String view(WebRequest request, Model model)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		model.addAttribute("user", new UserForm(userOptional.get()));
		model.addAttribute("toast", WebRequestUtils.popToast(request));

		return "users/edit";
	}

	@PostMapping
	public String edit(WebRequest request, @ModelAttribute("user") UserForm userForm)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		try
		{
			userService.editUser(userOptional.get(), userForm);
		}
		catch(PasswordValidationException e)
		{
			WebRequestUtils.putToast(request, new Toast("Passwort Validierung fehlgeschlagen", ToastColor.DANGER));
			return "redirect:/user";
		}

		WebRequestUtils.putToast(request, new Toast("Änderungen gespeichert", ToastColor.SUCCESS));
		return "redirect:/user";
	}

	@GetMapping("shows")
	public String getShows(Model model)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final User user = userOptional.get();
		model.addAttribute("shows", user.getShows());

		return "users/shows";
	}
}
