package de.thecodelabs.pockettracker.authentication.api;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiKeyAuthenticationManager implements AuthenticationManager
{
	private final UserService userService;

	@Autowired
	public ApiKeyAuthenticationManager(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		final String credentials = (String) authentication.getCredentials();
		if(credentials == null)
		{
			throw new BadCredentialsException("No API Key provided");
		}

		final Optional<User> userOptional = userService.getUserByAccessToken(credentials);
		if(userOptional.isEmpty())
		{
			throw new BadCredentialsException("The API key was not found or not the expected value.");
		}
		authentication.setAuthenticated(true);
		return authentication;

	}
}
