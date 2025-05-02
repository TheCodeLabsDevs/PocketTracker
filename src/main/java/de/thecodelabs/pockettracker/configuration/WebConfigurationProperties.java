package de.thecodelabs.pockettracker.configuration;

import de.thecodelabs.utils.util.OS;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("pockettracker.web")
public class WebConfigurationProperties
{
	private String baseUrl;

	private String imageResourcePath;
	private String apiResourcesUrl = "/api/resources";
	private String webResourcesUrl = "/resources";

	public String getImageResourcePathForOS()
	{
		if(OS.isWindows())
		{
			return imageResourcePath.replaceFirst("^/(.:/)", "$1");
		}

		return imageResourcePath;
	}
}
