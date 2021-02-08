package de.thecodelabs.pockettracker.utils.toast;

import de.thecodelabs.pockettracker.utils.BootstrapColor;
import org.springframework.context.MessageSourceResolvable;

public class Toast implements MessageSourceResolvable
{
	private final String message;
	private final BootstrapColor color;
	private final Object[] args;

	public Toast(String message, BootstrapColor color, Object... args)
	{
		this.message = message;
		this.color = color;
		this.args = args;
	}

	public String getMessage()
	{
		return message;
	}

	public BootstrapColor getColor()
	{
		return color;
	}

	@Override
	public String[] getCodes()
	{
		return new String[] {message};
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
