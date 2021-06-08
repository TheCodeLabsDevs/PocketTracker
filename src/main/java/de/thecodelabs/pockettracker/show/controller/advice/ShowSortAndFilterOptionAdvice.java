package de.thecodelabs.pockettracker.show.controller.advice;

import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ShowSortAndFilterOptionAdvice
{

	@ModelAttribute("showFilterOptions")
	public ShowFilterOption[] showFilterOptions()
	{
		return ShowFilterOption.values();
	}

	@ModelAttribute("showSortOptions")
	public ShowSortOption[] showSortOptions()
	{
		return ShowSortOption.values();
	}
}
