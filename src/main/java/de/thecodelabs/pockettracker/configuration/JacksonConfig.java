package de.thecodelabs.pockettracker.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.support.JacksonHandlerInstantiator;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class JacksonConfig
{
	@Bean
	public JsonMapper objectMapper(ApplicationContext applicationContext)
	{
		return JsonMapper.builder()
				.handlerInstantiator(
						new JacksonHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory())
				)
				.build();
	}
}