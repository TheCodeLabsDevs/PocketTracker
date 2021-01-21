package de.thecodelabs.pockettracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class PocketTrackerMain
{
	public static void main(String[] args)
	{
		SpringApplication.run(PocketTrackerMain.class, args);
	}
}
