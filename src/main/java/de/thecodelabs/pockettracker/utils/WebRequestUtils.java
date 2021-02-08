package de.thecodelabs.pockettracker.utils;

import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

public class WebRequestUtils
{
	private static final String ATTR_TOAST = "toast";
	private static final String ATTR_VALIDATION_ERROR = "validation-error";
	private static final String ATTR_VALIDATION_DATA = "validation-data";

	private WebRequestUtils()
	{
	}

	public static void putToast(WebRequest request, Toast toast)
	{
		put(request, toast, ATTR_TOAST);
	}

	public static Toast popToast(WebRequest request)
	{
		return (Toast) pop(request, ATTR_TOAST);
	}

	public static void putValidationError(WebRequest request, BindingResult result, Object data)
	{
		put(request, result, ATTR_VALIDATION_ERROR);
		put(request, data, ATTR_VALIDATION_DATA);
	}

	public static BindingResult popValidationError(WebRequest request)
	{
		return (BindingResult) pop(request, ATTR_VALIDATION_ERROR);
	}

	public static Object popValidationData(WebRequest request)
	{
		return pop(request, ATTR_VALIDATION_DATA);
	}

	private static void put(WebRequest request, Object any, String key)
	{
		request.setAttribute(key, any, RequestAttributes.SCOPE_SESSION);
	}

	public static Object pop(WebRequest request, String key)
	{
		final Object any = request.getAttribute(key, RequestAttributes.SCOPE_SESSION);
		request.removeAttribute(key, RequestAttributes.SCOPE_SESSION);

		return any;
	}
}
