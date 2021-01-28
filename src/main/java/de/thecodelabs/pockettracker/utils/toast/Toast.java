package de.thecodelabs.pockettracker.utils.toast;

public class Toast
{
	private String message;
	private ToastColor color;

	public Toast(String message, ToastColor color)
	{
		this.message = message;
		this.color = color;
	}

	public String getMessage()
	{
		return message;
	}

	public ToastColor getColor()
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
