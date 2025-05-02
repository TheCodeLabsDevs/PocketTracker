package de.thecodelabs.pockettracker.utils.toast;

import de.thecodelabs.pockettracker.utils.BootstrapColor;
import org.springframework.context.MessageSourceResolvable;

public record Toast(String message, BootstrapColor color, Object... args) implements MessageSourceResolvable
{
	@Override
	public String[] getCodes()
	{
		return new String[]{message};
	}

	@Override
	public Object[] getArguments()
	{
		return args;
	}

	@Override
	public String toString()
	{
		return "Toast{" +
				"message='" + message + '\'' +
				", color=" + color +
				'}';
	}
}
