package de.thecodelabs.pockettracker.authentication.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@RequiredArgsConstructor
public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter
{
	private final String principalRequestHeader;

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request)
	{
		return "api-token";
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request)
	{
		return request.getHeader(principalRequestHeader);
	}

}
