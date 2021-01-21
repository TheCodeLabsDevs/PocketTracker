package de.thecodelabs.pockettracker.user;

public enum UserType
{
	ADMIN,
	USER;

	public String getRoleName() {
		return this.name();
	}
}
