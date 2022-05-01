package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
	Optional<User> findUserByName(@Param("name") String name);

	Optional<User> findUserByAuthenticationsContains(@Param("authentication") UserAuthentication authentication);

	Optional<User> findUserByTokens_token(@Param("token") String token);
}
