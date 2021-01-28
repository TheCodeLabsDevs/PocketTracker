package de.thecodelabs.pockettracker.utils;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class BindingResultAdvice
{
	@ModelAttribute("validation")
	public BindingResult validation(WebRequest request)
	{
		return WebRequestUtils.popValidationError(request);
	}
}
