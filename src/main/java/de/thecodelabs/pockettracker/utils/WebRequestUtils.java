package de.thecodelabs.pockettracker.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

public class WebRequestUtils
{
	private static final String ATTR_TOAST = "toast";

	private WebRequestUtils()
	{
	}

	public static void putToast(WebRequest request, Toast toast)
	{
		request.setAttribute(ATTR_TOAST, toast, RequestAttributes.SCOPE_SESSION);
	}

	public static Toast popToast(WebRequest request)
	{
		final Object toast = request.getAttribute(ATTR_TOAST, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(ATTR_TOAST, RequestAttributes.SCOPE_SESSION);

		return (Toast) toast;
	}
}
