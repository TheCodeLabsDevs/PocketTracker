package de.thecodelabs.pockettracker.user;

import de.thecodelabs.pockettracker.authentication.AuthenticationConfigurationProperties;
import de.thecodelabs.pockettracker.user.controller.UserForm;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserRole;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.utils.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class InitialUserCreator
{
	private static final int DEFAULT_PASSWORD_LENGTH = 20;

	private static final Logger log = LoggerFactory.getLogger(InitialUserCreator.class);

	private final UserService userService;
	private final AuthenticationConfigurationProperties configurationProperties;

	@EventListener
	public void applicationStarted(ApplicationStartedEvent event) throws PasswordValidationException
	{
		if(userService.hasUsers())
		{
			return;
		}

		final String password = RandomUtils.generateRandomString(RandomUtils.RandomType.DEFAULT, DEFAULT_PASSWORD_LENGTH, RandomUtils.RandomStringPolicy.LOWER, RandomUtils.RandomStringPolicy.DIGIT);
		try
		{
			final Path path = Paths.get(configurationProperties.getDefaultPasswordFolder(), "password.txt");
			Files.write(path, password.getBytes());
			log.info("Create default password in file: {}", path.toAbsolutePath());
		}
		catch(IOException e)
		{
			log.error("Cannot create default password file", e);
			return;
		}

		final UserForm userForm = new UserForm();
		userForm.setUsername("admin");
		userForm.setUserRole(UserRole.ADMIN);
		userForm.setPassword(password);
		userForm.setPasswordRepeat(password);

		final User user = userService.createUser(userForm);
		userService.addInternalAuthentication(user, userForm);
	}
}
