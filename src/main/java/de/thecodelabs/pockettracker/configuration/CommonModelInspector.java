package de.thecodelabs.pockettracker.configuration;

import de.thecodelabs.pockettracker.authentication.AuthenticationConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CommonModelInspector implements HandlerInterceptor
{
	private final AuthenticationConfigurationProperties properties;

	@Value("${pockettracker.version}")
	private String version;

	@Autowired
	public CommonModelInspector(AuthenticationConfigurationProperties properties)
	{
		this.properties = properties;
	}

	@Override
	public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
						   @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception
	{
		if(modelAndView == null)
		{
			return;
		}

		if(modelAndView.getViewName() == null || modelAndView.getViewName().startsWith("redirect:"))
		{
			return;
		}

		modelAndView.addObject("oauthEnabled", properties.isEnableOAuth());
		modelAndView.addObject("version", version);
	}
}
