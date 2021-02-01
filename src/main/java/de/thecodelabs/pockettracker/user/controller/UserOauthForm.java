package de.thecodelabs.pockettracker.user.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserOauthForm
{
	@NotNull
	@NotEmpty
	private String username;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
}
