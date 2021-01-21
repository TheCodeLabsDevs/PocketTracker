package de.thecodelabs.pockettracker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pockettracker.security")
public class AuthenticationConfigurationProperties
{
	private String defaultPasswordFolder;

	public String getDefaultPasswordFolder()
	{
		return defaultPasswordFolder;
	}

	public void setDefaultPasswordFolder(String defaultPasswordFolder)
	{
		this.defaultPasswordFolder = defaultPasswordFolder;
	}
}
