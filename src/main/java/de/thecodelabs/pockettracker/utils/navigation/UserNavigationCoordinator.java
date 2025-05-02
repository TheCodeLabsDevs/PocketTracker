package de.thecodelabs.pockettracker.utils.navigation;

import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

@NoArgsConstructor
public class UserNavigationCoordinator
{
	public static final String USER_SPECIFIC_NAVIGATION = "isUserSpecificView";

	public static boolean isUserSpecificNavigation(WebRequest request)
	{
		return Optional.ofNullable(request.getAttribute(USER_SPECIFIC_NAVIGATION, RequestAttributes.SCOPE_SESSION))
				.map(Boolean.class::cast)
				.orElse(true);
	}

	public static void setUserSpecificNavigation(WebRequest request, boolean userSpecific)
	{
		request.setAttribute(USER_SPECIFIC_NAVIGATION, userSpecific, RequestAttributes.SCOPE_SESSION);
	}
}
