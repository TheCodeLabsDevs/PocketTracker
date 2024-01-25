package de.thecodelabs.pockettracker.user.service;

import de.thecodelabs.pockettracker.exceptions.ForbiddenException;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserAuthenticationService
{
	private final UserAuthenticationRepository authenticationRepository;

	@Autowired
	public UserAuthenticationService(UserAuthenticationRepository authenticationRepository)
	{
		this.authenticationRepository = authenticationRepository;
	}

	@Transactional
	public void deleteAuthenticationProvider(User user, UUID providerId)
	{
		final boolean anyMatch = user.getAuthentications().stream().anyMatch(provider -> provider.getId().equals(providerId));
		if(!anyMatch)
		{
			throw new ForbiddenException("Not your authentication");
		}

		user.getAuthentications().removeIf(provider -> provider.getId().equals(providerId));
		authenticationRepository.deleteById(providerId);
	}
}
