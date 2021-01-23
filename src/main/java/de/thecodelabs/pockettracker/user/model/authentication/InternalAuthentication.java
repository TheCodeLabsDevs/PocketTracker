package de.thecodelabs.pockettracker.user.model.authentication;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("internal")
public class InternalAuthentication extends UserAuthentication
{
	private String password;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
