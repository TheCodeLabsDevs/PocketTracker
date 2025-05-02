package de.thecodelabs.pockettracker.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.user.service.InternalAuthenticationService;
import de.thecodelabs.pockettracker.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RememberMeService implements PersistentTokenRepository
{
	private final UserService userService;
	private final InternalAuthenticationService internalAuthenticationService;

	@Override
	@Transactional
	public void createNewToken(PersistentRememberMeToken persistentRememberMeToken)
	{
		final Optional<User> userOptional = userService.getUserByInternalAuthentication(persistentRememberMeToken.getUsername());
		if(userOptional.isEmpty())
		{
			return;
		}

		InternalAuthentication authentication = userOptional.get().getAuthentication(InternalAuthentication.class).orElseThrow();
		authentication.setRememberMe(persistentRememberMeToken);
	}

	@Override
	@Transactional
	public void updateToken(String series, String tokenValue, Date lastUsed)
	{
		final Optional<InternalAuthentication> authenticationOptional = internalAuthenticationService.getInternalAuthenticationByRememberMeSeries(series);
		if(authenticationOptional.isEmpty())
		{
			return;
		}

		final InternalAuthentication authentication = authenticationOptional.get();
		authentication.setRememberMeToken(tokenValue);
		authentication.setRememberMeDate(lastUsed);
	}

	@Override
	@Transactional
	public PersistentRememberMeToken getTokenForSeries(String series)
	{
		final Optional<InternalAuthentication> authenticationOptional = internalAuthenticationService.getInternalAuthenticationByRememberMeSeries(series);
		if(authenticationOptional.isEmpty())
		{
			return null;
		}

		final InternalAuthentication authentication = authenticationOptional.get();
		final Optional<User> userOptional = userService.getUserByUserAuthentication(authentication);

		if(userOptional.isEmpty())
		{
			return null;
		}

		final LocalDateTime rememberMeDate = authentication.getRememberMeDate();
		return new PersistentRememberMeToken(
				userOptional.get().getName(),
				authentication.getRememberMeSeries(),
				authentication.getRememberMeToken(),
				Date.from(rememberMeDate.atZone(ZoneId.systemDefault()).toInstant())
		);
	}

	@Override
	@Transactional
	public void removeUserTokens(String username)
	{
		final Optional<User> userOptional = userService.getUserByInternalAuthentication(username);
		if(userOptional.isEmpty())
		{
			return;
		}

		final User user = userOptional.get();
		final InternalAuthentication authentication = user.getAuthentication(InternalAuthentication.class).orElseThrow();
		authentication.setRememberMe(null);
	}
}
