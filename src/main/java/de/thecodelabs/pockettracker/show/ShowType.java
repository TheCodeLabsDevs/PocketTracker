package de.thecodelabs.pockettracker.show;

import org.springframework.context.MessageSourceResolvable;

public enum ShowType implements MessageSourceResolvable
{
	TV,
	AUDIO;


	@Override
	public String[] getCodes()
	{
		return new String[]{"showType." + name()};
	}
}
