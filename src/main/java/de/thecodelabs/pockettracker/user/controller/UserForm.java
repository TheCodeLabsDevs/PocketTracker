package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.user.User;
import de.thecodelabs.pockettracker.user.UserType;

import java.util.Optional;

public class UserForm
{
	private String username;
	private String password;
	private String passwordRepeat;
	private UserType userType;

	public UserForm()
	{
	}

	public UserForm(User user)
	{
		this.username = user.getName();
		this.userType = user.getUserType();
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

	public UserType getUserType()
	{
		return userType;
	}

	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}
}
