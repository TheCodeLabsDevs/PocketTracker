package de.thecodelabs.pockettracker.user;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.season.Season;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.user.controller.UserForm;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.user.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

		if(authentication instanceof OAuth2AuthenticationToken)
		{
			// Get Gitlab user or create new account
			return getUser(authentication.getName(), UserType.GITLAB).or(() -> {
				final UserForm userForm = new UserForm();
				userForm.setUsername(authentication.getName());
				try
				{
					return Optional.of(createUser(userForm, UserType.GITLAB));
				}
				catch(PasswordValidationException ignored)
				{
					return Optional.empty();
				}
			});
		}
		else if(authentication instanceof UsernamePasswordAuthenticationToken)
		{
			return getUser(authentication.getName(), UserType.INTERNAL);
		}
		return Optional.empty();
	}

	public Optional<User> getUser(String username, UserType userType)
	{
		return userRepository.findUserByNameAndUserType(username, userType);
	}

	public Optional<User> getUser(Integer id)
	{
		return userRepository.findById(id);
	}

	public List<User> getUsers()
	{
		return userRepository.findAll();
	}

	public User createUser(UserForm userForm, UserType userType) throws PasswordValidationException
	{
		User user = new User();
		user.setName(userForm.getUsername());
		user.setUserRole(userForm.getUserRole());
		user.setUserType(userType);

		if(user.getUserRole() == null)
		{
			user.setUserRole(UserRole.USER);
		}

		if(userType.isPasswordValidation())
		{
			if(!validatePassword(userForm))
			{
				throw new PasswordValidationException();
			}
			user.setPassword(passwordEncoder.encode(userForm.getPassword()));
		}

		return userRepository.save(user);
	}

	public User editUser(User user, UserForm userForm) throws PasswordValidationException
	{
		user.setName(userForm.getUsername());

		// Admin only changes
		getCurrentUser().filter(u -> u.getUserRole() == UserRole.ADMIN).ifPresent(u -> {
			if(userForm.getUserRole() != null)
			{
				user.setUserRole(userForm.getUserRole());
			}
		});

		final String password = userForm.getPassword();
		if(password != null && !password.isEmpty() && user.getUserType().isPasswordValidation())
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

	public List<Episode> getWatchedEpisodesBySeason(User user, Season season)
	{
		return user.getWatchedEpisodes().stream()
				.filter(episode -> episode.getSeason().equals(season))
				.collect(Collectors.toList());
	}
}
