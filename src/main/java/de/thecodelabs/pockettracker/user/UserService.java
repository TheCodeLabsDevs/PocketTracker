package de.thecodelabs.pockettracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

	public User createUser(User user, String password) {
		if (user.getUserType() == null) {
			user.setUserType(UserType.USER);
		}
		user.setPassword(passwordEncoder.encode(password));
		return userRepository.save(user);
	}
}
