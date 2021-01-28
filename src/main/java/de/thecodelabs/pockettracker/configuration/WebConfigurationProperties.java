package de.thecodelabs.pockettracker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pockettracker.web")
public class WebConfigurationProperties
{
	private String baseUrl;

	private String imageResourcePath;
	private String apiResourcesUrl = "/api/resources";
	private String webResourcesUrl = "/resources";

	public String getBaseUrl()
	{
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	public String getImageResourcePath()
	{
		return imageResourcePath;
	}

	public void setImageResourcePath(String imageResourcePath)
	{
		this.imageResourcePath = imageResourcePath;
	}

	public String getApiResourcesUrl()
	{
		return apiResourcesUrl;
	}

	public void setApiResourcesUrl(String apiResourcesUrl)
	{
		this.apiResourcesUrl = apiResourcesUrl;
	}

	public String getWebResourcesUrl()
	{
		return webResourcesUrl;
	}

	public void setWebResourcesUrl(String webResourcesUrl)
	{
		this.webResourcesUrl = webResourcesUrl;
	}
}
