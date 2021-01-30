package de.thecodelabs.pockettracker.user.controller.advice;

import de.thecodelabs.pockettracker.user.model.UserRole;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserRoleAdvice
{
	@ModelAttribute("userRoles")
	public UserRole[] userRoles()
	{
		return UserRole.values();
	}
}
