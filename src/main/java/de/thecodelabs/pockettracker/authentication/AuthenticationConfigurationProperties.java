package de.thecodelabs.pockettracker.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("pockettracker.security")
public class AuthenticationConfigurationProperties
{
	private String defaultPasswordFolder;

	private int rememberMeTokenValiditySeconds;

	private boolean enableOAuth = false;
}
