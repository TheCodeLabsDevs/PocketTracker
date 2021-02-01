package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users/administration")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class UserAdministrationController
{
	private final UserService userService;

	@Autowired
	public UserAdministrationController(UserService userService)
	{
		this.userService = userService;
	}

	@GetMapping
	public String index(Model model)
	{
		model.addAttribute("users", userService.getUsers());
		return "administration/user/index";
	}

	@GetMapping("/add")
	public String addView(Model model)
	{
		model.addAttribute("user", new UserForm());
		return "administration/user/add";
	}

	@PostMapping("/add")
	public String addSubmit(@ModelAttribute("user") UserForm userForm)
	{
		try
		{
			final User user = userService.createUser(userForm);
			userService.addInternalAuthentication(user, userForm);
		}
		catch(PasswordValidationException e)
		{
			return "redirect:/users/administration/add";
		}

		return "redirect:/users/administration";
	}


	@GetMapping("/{id}/edit")
	public String editView(@PathVariable Integer id, Model model)
	{
		final Optional<User> userOptional = userService.getUserById(id);
		if(userOptional.isEmpty())
		{
			throw new NotFoundException();
		}

		model.addAttribute("user", new UserForm(userOptional.get()));
		return "administration/user/edit";
	}


	@PostMapping("/{id}/edit")
	public String editSubmit(@PathVariable Integer id, @ModelAttribute("user") UserForm userForm)
	{
		final Optional<User> userOptional = userService.getUserById(id);
		if(userOptional.isEmpty())
		{
			throw new NotFoundException();
		}
		final User user = userOptional.get();
		try
		{
			userService.editUser(user, userForm);
		}
		catch(PasswordValidationException e)
		{
			return "redirect:/users/administration/" + id + "/edit";
		}

		return "redirect:/users/administration";
	}

	@GetMapping("/{id}/delete")
	public String deleteView(@PathVariable Integer id, Model model)
	{
		final Optional<User> userOptional = userService.getUserById(id);
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		model.addAttribute("user", userOptional.get());
		return "administration/user/delete";
	}

	@PostMapping("/{id}/delete")
	public String deleteSubmit(@PathVariable Integer id)
	{
		final Optional<User> userOptional = userService.getUserById(id);
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		userService.deleteUser(userOptional.get());
		return "redirect:/users/administration";
	}
}
