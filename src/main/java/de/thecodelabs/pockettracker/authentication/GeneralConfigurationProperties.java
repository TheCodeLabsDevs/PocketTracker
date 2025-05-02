package de.thecodelabs.pockettracker.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("pockettracker.general")
public class GeneralConfigurationProperties
{
	private String language;
}
