package de.thecodelabs.pockettracker.utils.toast;

import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ToastAdvice
{
	@ModelAttribute("toast")
	public Toast getToast(WebRequest request) {
		return WebRequestUtils.popToast(request);
	}
}
