package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InternalAuthenticationRepository extends JpaRepository<InternalAuthentication, Integer>
{
	Optional<InternalAuthentication> findInternalAuthenticationByRememberMeSeries(String series);
}
