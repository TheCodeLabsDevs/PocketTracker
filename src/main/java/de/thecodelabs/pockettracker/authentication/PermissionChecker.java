package de.thecodelabs.pockettracker.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("perm")
public class PermissionChecker
{
	private final UserService service;

	@Autowired
	public PermissionChecker(UserService service)
	{
		this.service = service;
	}

	public boolean hasPermission(UserRole role)
	{
		final Optional<User> userOptional = service.getUser(SecurityContextHolder.getContext().getAuthentication());
		if(userOptional.isEmpty())
		{
			return false;
		}
		return userOptional.get().getUserRole() == role;
	}
}
