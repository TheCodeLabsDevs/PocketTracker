package de.thecodelabs.pockettracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService
{
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Optional<User> getUser(Authentication authentication)
	{
		if(authentication == null)
		{
			return Optional.empty();
		}
		return getUser(authentication.getName());
	}

	public Optional<User> getUser(String username)
	{
		return userRepository.findUserByName(username);
	}

	public User createUser(User user, String password)
	{
		if(user.getUserType() == null)
		{
			user.setUserType(UserType.USER);
		}
		user.setPassword(passwordEncoder.encode(password));
		return userRepository.save(user);
	}

	public boolean hasUsers()
	{
		return userRepository.count() > 0;
	}
}
