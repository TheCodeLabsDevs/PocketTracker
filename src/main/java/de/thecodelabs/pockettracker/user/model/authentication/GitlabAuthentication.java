package de.thecodelabs.pockettracker.user.model.authentication;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("gitlab")
public class GitlabAuthentication extends UserAuthentication
{
	private String gitlabUsername;

	public String getGitlabUsername()
	{
		return gitlabUsername;
	}

	public void setGitlabUsername(String gitlabUsername)
	{
		this.gitlabUsername = gitlabUsername;
	}
}
