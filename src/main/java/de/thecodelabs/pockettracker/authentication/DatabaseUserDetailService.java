package de.thecodelabs.pockettracker.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class DatabaseUserDetailService implements UserDetailsService
{
	private final UserService userService;

	@Autowired
	public DatabaseUserDetailService(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws AuthenticationException
	{
		final Optional<User> userOptional = userService.getUserByInternalAuthentication(username);
		if(userOptional.isEmpty())
		{
			throw new UsernameNotFoundException(username);
		}

		final User user = userOptional.get();
		final Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getUserRole().getRoleName()));
		final String password = user.getAuthentication(InternalAuthentication.class).orElseThrow().getPassword();
		return new org.springframework.security.core.userdetails.User(user.getName(), password, authorities);
	}
}
