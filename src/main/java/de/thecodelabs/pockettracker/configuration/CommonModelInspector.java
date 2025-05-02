package de.thecodelabs.pockettracker.configuration;

import de.thecodelabs.pockettracker.authentication.AuthenticationConfigurationProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class CommonModelInspector implements HandlerInterceptor
{
	private final AuthenticationConfigurationProperties properties;

	@Value("${pockettracker.version}")
	private String version;

	@Override
	public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
						   @NonNull Object handler, @Nullable ModelAndView modelAndView)
	{
		if(modelAndView == null)
		{
			return;
		}

		final String viewName = modelAndView.getViewName();
		if(viewName == null || viewName.startsWith("redirect:"))
		{
			return;
		}

		modelAndView.addObject("oauthEnabled", properties.isEnableOAuth());
		modelAndView.addObject("version", version);
	}
}
