package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Integer>
{
}
