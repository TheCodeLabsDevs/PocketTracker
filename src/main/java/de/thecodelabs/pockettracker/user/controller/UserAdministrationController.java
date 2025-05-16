package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/users/administration")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
@RequiredArgsConstructor
public class UserAdministrationController
{
	private final UserService userService;

	private static class ReturnValues
	{
		public static final String ADMIN_USER_INDEX = "administration/user/index";
		public static final String ADMIN_USER_ADD = "administration/user/add";
		public static final String ADMIN_USER_EDIT = "administration/user/edit";
		public static final String REDIRECT_ADMIN_USER_ADD = "redirect:/users/administration/add";
		public static final String REDIRECT_ADMIN_USERS = "redirect:/users/administration";
	}

	@GetMapping
	public String index(Model model)
	{
		model.addAttribute("users", userService.getUsers());
		return ReturnValues.ADMIN_USER_INDEX;
	}

	@GetMapping("/add")
	public String addView(Model model)
	{
		model.addAttribute("user", new UserForm());
		return ReturnValues.ADMIN_USER_ADD;
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
			return ReturnValues.REDIRECT_ADMIN_USER_ADD;
		}

		return ReturnValues.REDIRECT_ADMIN_USERS;
	}


	@GetMapping("/{id}/edit")
	public String editView(@PathVariable UUID id, Model model)
	{
		final Optional<User> userOptional = userService.getUserById(id);
		if(userOptional.isEmpty())
		{
			throw new NotFoundException();
		}

		model.addAttribute("user", new UserForm(userOptional.get()));
		return ReturnValues.ADMIN_USER_EDIT;
	}


	@PostMapping("/{id}/edit")
	public String editSubmit(@PathVariable UUID id, @ModelAttribute("user") UserForm userForm)
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

		return ReturnValues.REDIRECT_ADMIN_USERS;
	}

	@PostMapping("/{id}/delete")
	public String deleteSubmit(@PathVariable UUID id)
	{
		final Optional<User> userOptional = userService.getUserById(id);
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		userService.deleteUser(userOptional.get());
		return ReturnValues.REDIRECT_ADMIN_USERS;
	}
}
