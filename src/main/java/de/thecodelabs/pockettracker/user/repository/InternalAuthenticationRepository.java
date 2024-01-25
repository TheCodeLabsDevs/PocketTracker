package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.authentication.InternalAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InternalAuthenticationRepository extends JpaRepository<InternalAuthentication, UUID>
{
	Optional<InternalAuthentication> findInternalAuthenticationByRememberMeSeries(@Param("series") String series);
}
