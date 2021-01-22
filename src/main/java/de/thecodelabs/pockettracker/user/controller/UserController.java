package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.user.User;
import de.thecodelabs.pockettracker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String view(Model model)
	{
		final Optional<User> userOptional = userService.getUser(SecurityContextHolder.getContext().getAuthentication());
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("Own user not found");
		}

		model.addAttribute("user", new UserForm(userOptional.get()));

		return "users/edit";
	}

	@PostMapping
	public String edit(@ModelAttribute("user") UserForm userForm)
	{
		final Optional<User> userOptional = userService.getUser(SecurityContextHolder.getContext().getAuthentication());
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("Own user not found");
		}

		userService.editUser(userOptional.get(), userForm);

		return "redirect:/user";
	}
}
