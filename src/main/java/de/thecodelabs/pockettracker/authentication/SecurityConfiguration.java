package de.thecodelabs.pockettracker.authentication;

import de.thecodelabs.pockettracker.authentication.api.ApiKeyAuthFilter;
import de.thecodelabs.pockettracker.authentication.api.ApiKeyAuthenticationManager;
import de.thecodelabs.pockettracker.authentication.api.ApiSecurityConfigurationProperties;
import de.thecodelabs.pockettracker.user.controller.UserForm;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
public class SecurityConfiguration
{
	private static final String[] PERMIT_ALL = {"/webjars/**", "/css/**", "/js/**", "/lang/**", "/image/**", "/touch_icon.png"};
	private static final String[] AUTHENTICATED = {"/**"};
	private static final String LOGIN_PAGE = "/login";

	private final RememberMeService rememberMeService;
	private final AuthenticationConfigurationProperties configurationProperties;

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private final UserService userService;

	private final ApiKeyAuthenticationManager authenticationManager;
	private final ApiSecurityConfigurationProperties apiConfigurationProperties;

	@Autowired
	public SecurityConfiguration(RememberMeService rememberMeService, AuthenticationConfigurationProperties configurationProperties,
								 UserService userService, ApiKeyAuthenticationManager authenticationManager,
								 ApiSecurityConfigurationProperties apiConfigurationProperties)
	{
		this.rememberMeService = rememberMeService;
		this.configurationProperties = configurationProperties;
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.apiConfigurationProperties = apiConfigurationProperties;
	}

	@Order(2)
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http
				.csrf()
				.and()

				.authorizeRequests()
				.antMatchers(PERMIT_ALL).permitAll()
				.antMatchers(AUTHENTICATED).authenticated()
				.and()

				.formLogin()
				.loginPage(LOGIN_PAGE)
				.permitAll()
				.and()

				.rememberMe()
				.tokenRepository(rememberMeService)
				.tokenValiditySeconds(configurationProperties.getRememberMeTokenValiditySeconds())
				.and()

				.logout()
				.permitAll()
				.and();

		if(configurationProperties.isEnableOAuth())
		{
			http
					.oauth2Login()
					.successHandler((httpServletRequest, httpServletResponse, authentication) -> {
						// Create user if missing
						final Optional<User> userOptional = userService.getUser(authentication);
						if(userOptional.isEmpty())
						{
							try
							{
								final UserForm userForm = new UserForm();
								userForm.setUsername(authentication.getName());
								final User user = userService.createUser(userForm);
								userService.addGitlabAuthentication(user, authentication.getName());
							}
							catch(Exception e)
							{
								throw new BadCredentialsException("Fail to create Gitlab Authentication");
							}
						}

						redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
					})
					.loginPage(LOGIN_PAGE)
					.permitAll();
		}
		return http.build();
	}

	@Order(1)
	@Bean
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception
	{
		final ApiKeyAuthFilter filter = new ApiKeyAuthFilter(apiConfigurationProperties.getHeaderName());
		filter.setAuthenticationManager(authenticationManager);

		http
				.antMatcher("/api/**")

				.csrf()
				.disable()

				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()

				.addFilter(filter)
				.authorizeRequests().anyRequest().authenticated();
		return http.build();
	}
}
