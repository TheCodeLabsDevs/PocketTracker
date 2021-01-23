package de.thecodelabs.pockettracker.configuration;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
	private final WebConfigurationProperties webConfigurationProperties;

	@Autowired
	public WebMvcConfig(WebConfigurationProperties webConfigurationProperties)
	{
		this.webConfigurationProperties = webConfigurationProperties;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		if(!registry.hasMappingForPattern("/webjars/**"))
		{
			registry.addResourceHandler("/webjars/**")
					.addResourceLocations("classpath:/META-INF/resources/webjars/");
		}

		if(!registry.hasMappingForPattern("/resources/**"))
		{
			registry.addResourceHandler("/resources/**")
					.addResourceLocations("file://" + webConfigurationProperties.getImageResourcePath() +
							(!webConfigurationProperties.getImageResourcePath().endsWith("/") ? "/" : ""));
		}
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addRedirectViewController("/", "/user/shows");
	}
}
