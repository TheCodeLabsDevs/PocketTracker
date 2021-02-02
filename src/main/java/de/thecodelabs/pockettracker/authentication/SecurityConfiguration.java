package de.thecodelabs.pockettracker.authentication;

import de.thecodelabs.pockettracker.user.controller.UserForm;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	private static final String[] PERMIT_ALL = {"/webjars/**", "/css/**", "/js/**", "/lang/**", "/image/**", "/touch_icon.png"};
	private static final String[] AUTHENTICATED = {"/**"};
	private static final String LOGIN_PAGE = "/login";

	private final BCryptPasswordEncoder passwordEncoder;
	private final DatabaseUserDetailService databaseUserDetailService;
	private final RememberMeService rememberMeService;
	private final AuthenticationConfigurationProperties configurationProperties;

	private final UserService userService;

	@Autowired
	public SecurityConfiguration(BCryptPasswordEncoder passwordEncoder, DatabaseUserDetailService databaseUserDetailService,
								 RememberMeService rememberMeService, AuthenticationConfigurationProperties configurationProperties,
								 UserService userService)
	{
		this.passwordEncoder = passwordEncoder;
		this.databaseUserDetailService = databaseUserDetailService;
		this.rememberMeService = rememberMeService;
		this.configurationProperties = configurationProperties;
		this.userService = userService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
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
								throw new UsernameNotFoundException("Fail to create Gitlab Authentication");
							}
						}

						httpServletResponse.sendRedirect("/");
					})
					.loginPage(LOGIN_PAGE)
					.permitAll();
		}
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(databaseUserDetailService)
				.passwordEncoder(passwordEncoder);
	}
}
