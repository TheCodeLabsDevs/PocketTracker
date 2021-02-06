package de.thecodelabs.pockettracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@OpenAPIDefinition(
		info = @Info(title = "PocketTracker", version = "1.0.0"),
		security = {
				@SecurityRequirement(name = "token")
		}
)
@SecurityScheme(
		name = "token",
		type = SecuritySchemeType.APIKEY,
		in = SecuritySchemeIn.HEADER,
		paramName = "api-key"
)
public class PocketTrackerMain
{
	public static void main(String[] args)
	{
		SpringApplication.run(PocketTrackerMain.class, args);
	}
}
