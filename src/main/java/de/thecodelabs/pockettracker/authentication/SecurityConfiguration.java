package de.thecodelabs.pockettracker.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	private static final String[] PERMIT_ALL = {"/webjars/**", "/css/**", "/js/**", "/lang/**", "/image/**"};
	private static final String[] AUTHENTICATED = {"/**"};
	private static final String LOGIN_PAGE = "/login";

	private final BCryptPasswordEncoder passwordEncoder;
	private final DatabaseUserDetailService databaseUserDetailService;
	private final RememberMeService rememberMeService;
	private final AuthenticationConfigurationProperties configurationProperties;

	@Autowired
	public SecurityConfiguration(BCryptPasswordEncoder passwordEncoder, DatabaseUserDetailService databaseUserDetailService,
								 RememberMeService rememberMeService, AuthenticationConfigurationProperties configurationProperties)
	{
		this.passwordEncoder = passwordEncoder;
		this.databaseUserDetailService = databaseUserDetailService;
		this.rememberMeService = rememberMeService;
		this.configurationProperties = configurationProperties;
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
