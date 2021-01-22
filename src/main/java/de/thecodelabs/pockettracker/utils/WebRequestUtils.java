package de.thecodelabs.pockettracker.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

public class WebRequestUtils
{
	private static final String ATTR_TOAST = "toast";

	private WebRequestUtils()
	{
	}

	public static void putToast(WebRequest request, String message)
	{
		request.setAttribute(ATTR_TOAST, message, RequestAttributes.SCOPE_SESSION);
	}

	public static String popToast(WebRequest request)
	{
		final Object attribute = request.getAttribute(ATTR_TOAST, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(ATTR_TOAST, RequestAttributes.SCOPE_SESSION);
		return (String) attribute;
	}
}
