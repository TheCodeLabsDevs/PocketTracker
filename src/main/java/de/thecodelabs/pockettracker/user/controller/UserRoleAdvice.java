package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.user.UserRole;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserRoleAdvice
{
	@ModelAttribute("userRoles")
	public List<String> userRoles()
	{
		return Arrays.stream(UserRole.values()).map(Enum::name).collect(Collectors.toList());
	}
}
