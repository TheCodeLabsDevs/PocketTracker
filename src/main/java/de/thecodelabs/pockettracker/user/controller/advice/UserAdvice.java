package de.thecodelabs.pockettracker.user.controller.advice;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class UserAdvice
{
	private final UserService userService;

	@ModelAttribute("currentUser")
	public User currentUser()
	{
		return userService.getUser(SecurityContextHolder.getContext().getAuthentication()).orElse(null);
	}

	@ModelAttribute("userService")
	public UserService userService()
	{
		return userService;
	}
}
