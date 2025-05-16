package de.thecodelabs.pockettracker.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
public class WebUtils
{
	private WebUtils()
	{
	}

	public static boolean isApiRequest()
	{
		final Optional<HttpServletRequest> currentHttpRequest = getCurrentHttpRequest();
		return currentHttpRequest.map(httpServletRequest -> httpServletRequest.getRequestURL().toString().contains("/api")).orElse(false);
	}

	public static Optional<HttpServletRequest> getCurrentHttpRequest()
	{
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(requestAttributes instanceof ServletRequestAttributes servletrequestattributes)
		{
			return Optional.of(servletrequestattributes.getRequest());
		}
		log.debug("Not called in the context of an HTTP request");
		return Optional.empty();
	}
}
