package de.thecodelabs.pockettracker.utils;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public enum BootstrapColor
{
	PRIMARY("primary"),
	SECONDARY("secondary"),
	SUCCESS("success"),
	DANGER("danger"),
	WARNING("warning"),
	INFO("info"),
	LIGHT("light"),
	DARK("dark");

	private static final String PATTERN = "{0}-{1}";
	private static final String PREFIX_TEXT = "text";
	private static final String PREFIX_BACKGROUND = "bg";
	private static final String PREFIX_TOAST = "alert";

	private final String styleClass;

	BootstrapColor(String styleClass)
	{
		this.styleClass = styleClass;
	}

	public String getTextColor()
	{
		return MessageFormat.format(PATTERN, PREFIX_TEXT, getStyleClass());
	}

	public String getBackgroundColor()
	{
		return MessageFormat.format(PATTERN, PREFIX_BACKGROUND, getStyleClass());
	}

	public String getToastColor()
	{
		return MessageFormat.format(PATTERN, PREFIX_TOAST, getStyleClass());
	}
}
