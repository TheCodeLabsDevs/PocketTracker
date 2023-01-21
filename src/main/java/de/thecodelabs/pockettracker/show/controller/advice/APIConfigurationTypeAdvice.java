package de.thecodelabs.pockettracker.show.controller.advice;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class APIConfigurationTypeAdvice
{
	@ModelAttribute("apiConfigurationTypes")
	public APIType[] apiConfigurationTypes()
	{
		return APIType.values();
	}
}
