package de.thecodelabs.pockettracker.show.controller.advice;

import de.thecodelabs.pockettracker.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class HelpersAdvice
{
	private final Helpers helpers;

	@Autowired
	public HelpersAdvice(Helpers helpers)
	{
		this.helpers = helpers;
	}

	@ModelAttribute(name = "helpers")
	public Helpers getHelpers()
	{
		return helpers;
	}
}
