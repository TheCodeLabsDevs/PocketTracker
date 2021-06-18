package de.thecodelabs.pockettracker.utils.navigation;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class UserNavigationAdvice
{
	@ModelAttribute("isUserSpecificViewNavigationTarget")
	public boolean isUserSpecificView(WebRequest request)
	{
		return UserNavigationCoordinator.isUserSpecificNavigation(request);
	}
}
