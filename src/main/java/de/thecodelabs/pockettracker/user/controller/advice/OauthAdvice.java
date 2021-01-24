package de.thecodelabs.pockettracker.user.controller.advice;

import de.thecodelabs.pockettracker.authentication.AuthenticationConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class OauthAdvice
{
	private final AuthenticationConfigurationProperties configurationProperties;

	@Autowired
	public OauthAdvice(AuthenticationConfigurationProperties configurationProperties)
	{
		this.configurationProperties = configurationProperties;
	}

	@ModelAttribute("oauthEnabled")
	public boolean isOauthEnabled() {
		return configurationProperties.isEnableOAuth();
	}
}
