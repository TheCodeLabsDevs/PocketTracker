package de.thecodelabs.pockettracker.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pockettracker.security")
public class AuthenticationConfigurationProperties
{
	private String defaultPasswordFolder;
	private int rememberMeTokenValiditySeconds;
	private boolean enableOAuth = false;

	public String getDefaultPasswordFolder()
	{
		return defaultPasswordFolder;
	}

	public void setDefaultPasswordFolder(String defaultPasswordFolder)
	{
		this.defaultPasswordFolder = defaultPasswordFolder;
	}

	public int getRememberMeTokenValiditySeconds()
	{
		return rememberMeTokenValiditySeconds;
	}

	public void setRememberMeTokenValiditySeconds(int rememberMeTokenValiditySeconds)
	{
		this.rememberMeTokenValiditySeconds = rememberMeTokenValiditySeconds;
	}

	public boolean isEnableOAuth()
	{
		return enableOAuth;
	}

	public void setEnableOAuth(boolean enableOAuth)
	{
		this.enableOAuth = enableOAuth;
	}
}
