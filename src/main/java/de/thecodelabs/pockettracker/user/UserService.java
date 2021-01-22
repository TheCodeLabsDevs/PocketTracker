package de.thecodelabs.pockettracker.user;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.user.controller.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public Optional<User> getCurrentUser()
	{
		return getUser(SecurityContextHolder.getContext().getAuthentication());
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

	public Optional<User> getUser(Integer id)
	{
		return userRepository.findById(id);
	}

	@PreAuthorize("hasAuthority(T(de.thecodelabs.pockettracker.user.UserType).ADMIN)")
	public List<User> getUsers()
	{
		return userRepository.findAll();
	}

	@PreAuthorize("hasAuthority(T(de.thecodelabs.pockettracker.user.UserType).ADMIN)")
	public User createUser(UserForm userForm) throws PasswordValidationException
	{
		User user = new User();
		user.setName(userForm.getUsername());
		user.setUserType(userForm.getUserType());

		if(user.getUserType() == null)
		{
			user.setUserType(UserType.USER);
		}

		if(!validatePassword(userForm))
		{
			throw new PasswordValidationException();
		}
		user.setPassword(passwordEncoder.encode(userForm.getPassword()));

		return userRepository.save(user);
	}

	public User editUser(User user, UserForm userForm) throws PasswordValidationException
	{
		user.setName(userForm.getUsername());

		// Admin only changes
		getCurrentUser().filter(u -> u.getUserType() == UserType.ADMIN).ifPresent(u -> {
			if(userForm.getUserType() != null)
			{
				user.setUserType(userForm.getUserType());
			}
		});

		final String password = userForm.getPassword();
		if(password != null && !password.isEmpty())
		{
			if(!validatePassword(userForm))
			{
				throw new PasswordValidationException();
			}

			user.setPassword(passwordEncoder.encode(password));
		}
		return userRepository.save(user);
	}

	public boolean hasUsers()
	{
		return userRepository.count() > 0;
	}

	public void deleteUser(User user)
	{
		userRepository.delete(user);
	}

	private boolean validatePassword(UserForm userForm)
	{
		if(userForm.getPassword().isEmpty())
		{
			return false;
		}

		if(!userForm.getPassword().equals(userForm.getPasswordRepeat()))
		{
			return false;
		}
		return true;
	}

	public List<Episode> getWatchedEpisodesByShow(User user, Show show)
	{
		return user.getWatchedEpisodes().stream()
				.filter(episode -> episode.getSeason().getShow().equals(show))
				.collect(Collectors.toList());
	}
}
