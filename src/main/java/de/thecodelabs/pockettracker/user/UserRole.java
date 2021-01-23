package de.thecodelabs.pockettracker.user;

public enum UserRole
{
	ADMIN,
	USER;

	public String getRoleName() {
		return this.name();
	}
}
