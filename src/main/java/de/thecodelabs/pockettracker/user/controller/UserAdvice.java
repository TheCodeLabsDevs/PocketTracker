package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.user.User;
import de.thecodelabs.pockettracker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserAdvice
{
	private final UserService userService;

	@Autowired
	public UserAdvice(UserService userService)
	{
		this.userService = userService;
	}

	@ModelAttribute("currentUser")
	public User currentUser() {
		return userService.getUser(SecurityContextHolder.getContext().getAuthentication()).orElse(null);
	}

	@ModelAttribute("userService")
	public UserService userService() {
		return userService;
	}
}
