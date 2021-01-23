package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.user.User;
import de.thecodelabs.pockettracker.user.UserRole;

public class UserForm
{
	private String username;
	private String password;
	private String passwordRepeat;
	private UserRole userRole;

	public UserForm()
	{
	}

	public UserForm(User user)
	{
		this.username = user.getName();
		this.userRole = user.getUserRole();
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPasswordRepeat()
	{
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat)
	{
		this.passwordRepeat = passwordRepeat;
	}

	public UserRole getUserRole()
	{
		return userRole;
	}

	public void setUserRole(UserRole userRole)
	{
		this.userRole = userRole;
	}
}
