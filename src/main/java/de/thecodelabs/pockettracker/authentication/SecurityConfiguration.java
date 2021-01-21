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
	private static final String[] PERMIT_ALL = {"/css/**", "/js/**", "/lang/**", "/image/**"};
	private static final String[] AUTHENTICATED = {"/**"};
	private static final String LOGIN_PAGE = "/login";

	private final BCryptPasswordEncoder passwordEncoder;
	private final DatabaseUserDetailService databaseUserDetailService;

	@Autowired
	public SecurityConfiguration(BCryptPasswordEncoder passwordEncoder, DatabaseUserDetailService databaseUserDetailService)
	{
		this.passwordEncoder = passwordEncoder;
		this.databaseUserDetailService = databaseUserDetailService;
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

				.httpBasic()
				.and()

				.logout()
				.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(databaseUserDetailService)
				.passwordEncoder(passwordEncoder);
	}
}
