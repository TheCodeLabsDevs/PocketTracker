package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.user.UserType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserTypeAdvice
{

	@ModelAttribute("userTypes")
	public List<String> userTypes()
	{
		return Arrays.stream(UserType.values()).map(Enum::name).collect(Collectors.toList());
	}
}
