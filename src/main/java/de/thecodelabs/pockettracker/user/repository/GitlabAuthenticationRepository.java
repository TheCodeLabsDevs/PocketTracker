package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GitlabAuthenticationRepository extends JpaRepository<GitlabAuthentication, Integer>
{
	Optional<GitlabAuthentication> findByGitlabUsername(@Param("series") String series);
}
