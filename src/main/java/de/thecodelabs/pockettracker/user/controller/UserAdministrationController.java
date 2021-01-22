package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.user.User;
import de.thecodelabs.pockettracker.user.UserService;
import de.thecodelabs.pockettracker.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users/administration")
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
		return "users/administration/index";
	}

	@GetMapping("/add")
	public String addView(Model model)
	{
		model.addAttribute("userTypes", Arrays.stream(UserType.values()).map(Objects::toString).collect(Collectors.toList()));
		return "users/administration/add";
	}

	@PostMapping("/add")
	public String addSubmit(@ModelAttribute("user") UserForm userForm)
	{
		if(userForm.getPassword().isEmpty())
		{
			return "redirect:/users/administration/add";
		}

		if(!userForm.getPassword().equals(userForm.getPasswordRepeat()))
		{
			return "redirect:/users/administration/add";
		}

		User user = new User();
		user.setName(userForm.getUsername());
		user.setUserType(userForm.getUserType());
		userService.createUser(user, userForm.getPassword());

		return "redirect:/users/administration";
	}


	@GetMapping("/{id}/delete")
	public String deleteView(@PathVariable Integer id, Model model)
	{
		final Optional<User> userOptional = userService.getUser(id);
		if (userOptional.isEmpty()) {
			throw new NotFoundException("User not found");
		}

		model.addAttribute("user", userOptional.get());
		return "users/administration/delete";
	}

	@PostMapping("/{id}/delete")
	public String deleteSubmit(@PathVariable Integer id, Model model)
	{
		final Optional<User> userOptional = userService.getUser(id);
		if (userOptional.isEmpty()) {
			throw new NotFoundException("User not found");
		}

		userService.deleteUser(userOptional.get());
		return "redirect:/users/administration";
	}
}
