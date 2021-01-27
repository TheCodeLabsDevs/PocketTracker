package de.thecodelabs.pockettracker.authentication.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(1)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter
{
	private final ApiKeyAuthenticationManager authenticationManager;
	private final ApiSecurityConfigurationProperties configurationProperties;

	@Autowired
	public ApiSecurityConfiguration(ApiKeyAuthenticationManager authenticationManager, ApiSecurityConfigurationProperties configurationProperties)
	{
		this.authenticationManager = authenticationManager;
		this.configurationProperties = configurationProperties;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception
	{
		ApiKeyAuthFilter filter = new ApiKeyAuthFilter(configurationProperties.getHeaderName());
		filter.setAuthenticationManager(authenticationManager);

		httpSecurity
				.antMatcher("/api/**")

				.csrf()
				.disable()

				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()

				.addFilter(filter)
				.authorizeRequests().anyRequest().authenticated();
	}
}