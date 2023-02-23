package de.thecodelabs.pockettracker.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class WebUtils
{
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

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
		logger.debug("Not called in the context of an HTTP request");
		return Optional.empty();
	}
}
