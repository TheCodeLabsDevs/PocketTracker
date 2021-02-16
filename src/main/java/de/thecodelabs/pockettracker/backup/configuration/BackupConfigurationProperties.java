package de.thecodelabs.pockettracker.backup.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pockettracker.backup")
public class BackupConfigurationProperties
{
	private String location;
	private Integer keep;

	private Boolean backgroundEnabled;
	private String backgroundScheduled;

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Integer getKeep()
	{
		return keep;
	}

	public void setKeep(Integer keep)
	{
		this.keep = keep;
	}

	public Boolean getBackgroundEnabled()
	{
		return backgroundEnabled;
	}

	public void setBackgroundEnabled(Boolean backgroundEnabled)
	{
		this.backgroundEnabled = backgroundEnabled;
	}

	public String getBackgroundScheduled()
	{
		return backgroundScheduled;
	}

	public void setBackgroundScheduled(String backgroundScheduled)
	{
		this.backgroundScheduled = backgroundScheduled;
	}
}
