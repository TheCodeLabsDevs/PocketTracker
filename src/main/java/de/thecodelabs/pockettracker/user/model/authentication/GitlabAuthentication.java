package de.thecodelabs.pockettracker.user.model.authentication;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("gitlab")
public class GitlabAuthentication extends UserAuthentication
{
	private String gitlabUsername;
}
