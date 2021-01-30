package de.thecodelabs.pockettracker.user.service;

import de.thecodelabs.pockettracker.exceptions.ForbiddenException;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.repository.UserAuthenticationRepository;
import de.thecodelabs.pockettracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService
{
	private final UserAuthenticationRepository authenticationRepository;
	private final UserRepository userRepository;

	@Autowired
	public UserAuthenticationService(UserAuthenticationRepository authenticationRepository, UserRepository userRepository)
	{
		this.authenticationRepository = authenticationRepository;
		this.userRepository = userRepository;
	}

	public void deleteAuthenticationProvider(User user, Integer providerId) {
		final boolean anyMatch = user.getAuthentications().stream().anyMatch(provider -> provider.getId().equals(providerId));
		if(!anyMatch)
		{
			throw new ForbiddenException("Not your authentication");
		}

		user.getAuthentications().removeIf(provider -> provider.getId().equals(providerId));

		userRepository.save(user);
		authenticationRepository.deleteById(providerId);
	}
}
