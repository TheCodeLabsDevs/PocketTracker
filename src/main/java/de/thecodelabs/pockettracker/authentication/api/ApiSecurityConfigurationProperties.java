package de.thecodelabs.pockettracker.authentication.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("pockettracker.security.api")
public class ApiSecurityConfigurationProperties
{
	private String headerName;
}
