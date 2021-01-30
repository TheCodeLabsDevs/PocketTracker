package de.thecodelabs.pockettracker.utils.toast;

import de.thecodelabs.pockettracker.utils.BootstrapColor;

public class Toast
{
	private String message;
	private BootstrapColor color;

	public Toast(String message, BootstrapColor color)
	{
		this.message = message;
		this.color = color;
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
	public String toString()
	{
		return "Toast{" +
				"message='" + message + '\'' +
				", color=" + color +
				'}';
	}
}
