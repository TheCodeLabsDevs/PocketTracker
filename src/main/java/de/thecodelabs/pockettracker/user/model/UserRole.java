package de.thecodelabs.pockettracker.user.model;

public enum UserRole
{
	ADMIN,
	USER;

	public String getRoleName() {
		return this.name();
	}
}
