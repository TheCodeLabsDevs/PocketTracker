package de.thecodelabs.pockettracker.show.controller.advice;

import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.Comparator;

@ControllerAdvice
public class ShowSortAndFilterOptionAdvice
{
	@ModelAttribute("showFilterOptions")
	public ShowFilterOption[] showFilterOptions()
	{
		return Arrays.stream(ShowFilterOption.values())
				.sorted(Comparator.comparingInt(ShowFilterOption::getOrder))
				.toArray(ShowFilterOption[]::new);
	}

	@ModelAttribute("showSortOptions")
	public ShowSortOption[] showSortOptions()
	{
		return ShowSortOption.values();
	}
}
