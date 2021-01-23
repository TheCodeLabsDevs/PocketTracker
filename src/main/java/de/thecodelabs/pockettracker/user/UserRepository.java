package de.thecodelabs.pockettracker.user;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
	Optional<User> findUserByNameAndUserType(String name, UserType userType);
}
