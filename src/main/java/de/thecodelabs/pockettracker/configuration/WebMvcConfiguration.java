package de.thecodelabs.pockettracker.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer
{
	private final WebConfigurationProperties webConfigurationProperties;
	private final CommonModelInspector commonModelInspector;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		if(!registry.hasMappingForPattern("/webjars/**"))
		{
			registry.addResourceHandler("/webjars/**")
					.addResourceLocations("classpath:/META-INF/resources/webjars/");
		}

		if(!registry.hasMappingForPattern(webConfigurationProperties.getWebResourcesUrl() + "/**"))
		{
			registry.addResourceHandler(webConfigurationProperties.getWebResourcesUrl() + "/**")
					.addResourceLocations("file://" + webConfigurationProperties.getImageResourcePath() +
							(!webConfigurationProperties.getImageResourcePath().endsWith("/") ? "/" : ""))
					.setCachePeriod(7 * 24 * 60 * 60); // 1 week
		}

		if(!registry.hasMappingForPattern(webConfigurationProperties.getApiResourcesUrl() + "/**"))
		{
			registry.addResourceHandler(webConfigurationProperties.getApiResourcesUrl() + "/**")
					.addResourceLocations("file://" + webConfigurationProperties.getImageResourcePath() +
							(!webConfigurationProperties.getImageResourcePath().endsWith("/") ? "/" : ""));
		}
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addRedirectViewController("/", "/user/shows");
		registry.addViewController("/login").setViewName("login");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(commonModelInspector);
	}
}
