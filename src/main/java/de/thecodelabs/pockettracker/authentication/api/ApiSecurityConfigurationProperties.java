package de.thecodelabs.pockettracker.authentication.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pockettracker.security.api")
public class ApiSecurityConfigurationProperties
{
	private String headerName;

	public String getHeaderName()
	{
		return headerName;
	}

	public void setHeaderName(String headerName)
	{
		this.headerName = headerName;
	}
}
