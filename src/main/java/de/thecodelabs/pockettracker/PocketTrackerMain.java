package de.thecodelabs.pockettracker;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.List;

@SpringBootApplication
@EnableMethodSecurity
@EnableScheduling
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

	@Bean
	public OpenAPI customOpenAPI(@Value("${pockettracker.version}") String appVersion)
	{
		return new OpenAPI()
				.components(new Components())
				.security(List.of(new SecurityRequirement().addList("token")))
				.info(new Info().title("PocketTracker").version(appVersion));
	}
}
