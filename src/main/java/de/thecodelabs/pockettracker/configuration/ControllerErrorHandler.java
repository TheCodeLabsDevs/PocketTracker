package de.thecodelabs.pockettracker.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.EnumMap;
import java.util.Map;

@Controller
public class ControllerErrorHandler implements ErrorController
{
	private final Map<HttpStatus, String> errorViews = new EnumMap<>(HttpStatus.class);

	@PostConstruct
	public void postInit()
	{
		errorViews.put(HttpStatus.BAD_REQUEST, "error/400");
		errorViews.put(HttpStatus.UNAUTHORIZED, "error/401");
		errorViews.put(HttpStatus.FORBIDDEN, "error/403");
		errorViews.put(HttpStatus.NOT_FOUND, "error/404");
		errorViews.put(HttpStatus.LOCKED, "error/423");
		errorViews.put(HttpStatus.INTERNAL_SERVER_ERROR, "error/500");
		errorViews.put(HttpStatus.SERVICE_UNAVAILABLE, "error/503");
	}

	@RequestMapping(path = "/error", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
	public ModelAndView handleError(HttpServletResponse response)
	{
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(errorViews.getOrDefault(HttpStatus.resolve(response.getStatus()), "error/500"));

		return modelAndView;
	}
}

