package de.thecodelabs.pockettracker.user.model;

import org.springframework.context.MessageSourceResolvable;

public enum UserRole implements MessageSourceResolvable
{
	ADMIN,
	USER;

	public String getRoleName()
	{
		return this.name();
	}

	@Override
	public String[] getCodes()
	{
		return new String[]{"role." + name()};
	}
}
