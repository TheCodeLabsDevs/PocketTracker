package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitlabAuthenticationRepository extends JpaRepository<GitlabAuthentication, Integer>
{
	Optional<GitlabAuthentication> findByGitlabUsername(String series);
}
