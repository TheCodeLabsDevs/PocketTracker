package de.thecodelabs.pockettracker.administration.apiconfiguration.model;

import org.springframework.context.MessageSourceResolvable;

public enum APIType implements MessageSourceResolvable
{
	TVDB_V3;

	@Override
	public String[] getCodes()
	{
		return new String[]{"apiConfigurationType." + name()};
	}
}
