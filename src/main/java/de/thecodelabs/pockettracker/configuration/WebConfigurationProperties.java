package de.thecodelabs.pockettracker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pockettracker.web")
public class WebConfigurationProperties
{
	private String imageResourcePath;

	public String getImageResourcePath()
	{
		return imageResourcePath;
	}

	public void setImageResourcePath(String imageResourcePath)
	{
		this.imageResourcePath = imageResourcePath;
	}
}
