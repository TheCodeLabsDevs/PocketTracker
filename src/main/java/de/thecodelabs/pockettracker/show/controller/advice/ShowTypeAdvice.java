package de.thecodelabs.pockettracker.show.controller.advice;

import de.thecodelabs.pockettracker.show.model.ShowType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ShowTypeAdvice
{
	@ModelAttribute("showTypes")
	public ShowType[] showTypes()
	{
		return ShowType.values();
	}
}
