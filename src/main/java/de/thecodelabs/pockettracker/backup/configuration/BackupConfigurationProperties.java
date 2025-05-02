package de.thecodelabs.pockettracker.backup.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pockettracker.backup")
@Getter
@Setter
public class BackupConfigurationProperties
{
	private String location;
	private Integer keep = 5;
	private Boolean includeImages = true;

	private Boolean backgroundEnabled;
	private String backgroundScheduled;
}
