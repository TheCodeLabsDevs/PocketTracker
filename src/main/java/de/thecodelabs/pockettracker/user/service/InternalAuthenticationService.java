package de.thecodelabs.pockettracker.user.service;

import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.user.repository.InternalAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternalAuthenticationService
{
	private final InternalAuthenticationRepository repository;

	public Optional<InternalAuthentication> getInternalAuthenticationByRememberMeSeries(String series)
	{
		return repository.findInternalAuthenticationByRememberMeSeries(series);
	}
}
