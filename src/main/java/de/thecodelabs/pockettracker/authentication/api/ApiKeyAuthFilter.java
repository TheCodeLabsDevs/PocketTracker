package de.thecodelabs.pockettracker.authentication.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter
{
	private final String principalRequestHeader;

	public ApiKeyAuthFilter(String principalRequestHeader)
	{
		this.principalRequestHeader = principalRequestHeader;
	}

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
