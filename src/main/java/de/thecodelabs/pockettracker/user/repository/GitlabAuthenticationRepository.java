package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface GitlabAuthenticationRepository extends JpaRepository<GitlabAuthentication, UUID>
{
	Optional<GitlabAuthentication> findByGitlabUsername(@Param("series") String series);
}
