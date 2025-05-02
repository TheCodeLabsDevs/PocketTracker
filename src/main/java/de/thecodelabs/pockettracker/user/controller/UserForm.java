package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserForm
{
	private String username;

	private String password;

	private String passwordRepeat;

	private UserRole userRole;

	public UserForm(User user)
	{
		this.username = user.getName();
		this.userRole = user.getUserRole();
	}
}
