package de.thecodelabs.pockettracker.user.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserOauthForm
{
	@NotNull
	@NotEmpty
	private String username;
}
