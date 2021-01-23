package de.thecodelabs.pockettracker.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		final Optional<User> userOptional = userService.getUser(username);

		if(userOptional.isEmpty())
		{
			throw new UsernameNotFoundException(username);
		}

		final User user = userOptional.get();
		final Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getUserRole().getRoleName()));
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
	}
}
