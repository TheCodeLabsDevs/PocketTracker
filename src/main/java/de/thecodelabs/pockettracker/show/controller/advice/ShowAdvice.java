package de.thecodelabs.pockettracker.show.controller.advice;

import de.thecodelabs.pockettracker.show.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ShowAdvice
{
	private final ShowService showService;

	@Autowired
	public ShowAdvice(ShowService showService)
	{
		this.showService = showService;
	}

	@ModelAttribute(name = "showService")
	public ShowService getShowService()
	{
		return showService;
	}
}
