package de.thecodelabs.pockettracker.user.model;

public enum UserType
{
	INTERNAL(true),
	GITLAB(false);

	private final boolean passwordValidation;

	UserType(boolean passwordValidation)
	{
		this.passwordValidation = passwordValidation;
	}

	public boolean isPasswordValidation()
	{
		return passwordValidation;
	}
}
