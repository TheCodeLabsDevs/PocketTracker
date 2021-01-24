package de.thecodelabs.pockettracker.user.service;

import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import de.thecodelabs.pockettracker.user.repository.InternalAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InternalAuthenticationService
{
	private final InternalAuthenticationRepository repository;

	@Autowired
	public InternalAuthenticationService(InternalAuthenticationRepository repository)
	{
		this.repository = repository;
	}

	public Optional<InternalAuthentication> getInternalAuthenticationByRememberMeSeries(String series) {
		return repository.findInternalAuthenticationByRememberMeSeries(series);
	}
}
