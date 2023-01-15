package de.thecodelabs.pockettracker.configuration;

import de.thecodelabs.pockettracker.authentication.GeneralConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Component
public class MessageSourceConfiguration implements WebMvcConfigurer
{
	private final GeneralConfigurationProperties generalConfigurationProperties;

	@Autowired
	public MessageSourceConfiguration(GeneralConfigurationProperties generalConfigurationProperties)
	{
		this.generalConfigurationProperties = generalConfigurationProperties;
	}

	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasenames(
				"classpath:lang/general"
		);
		source.setDefaultEncoding("UTF-8");
		return source;
	}

	@Bean
	public LocaleResolver localeResolver()
	{
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(new Locale(generalConfigurationProperties.getLanguage()));
		localeResolver.setCookieName("locale");
		localeResolver.setCookieMaxAge(4800);
		return localeResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("locale");
		registry.addInterceptor(localeChangeInterceptor);
	}

}
