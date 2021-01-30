package de.thecodelabs.pockettracker.user;

import de.thecodelabs.pockettracker.utils.BootstrapColor;


public class StatisticItem
{
	private final String icon;
	private final String text;
	private final BootstrapColor backgroundColor;
	private final BootstrapColor textColor;

	public StatisticItem(String icon, String text, BootstrapColor backgroundColor, BootstrapColor textColor)
	{
		this.icon = icon;
		this.text = text;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getText()
	{
		return text;
	}

	public BootstrapColor getBackgroundColor()
	{
		return backgroundColor;
	}

	public BootstrapColor getTextColor()
	{
		return textColor;
	}

	@Override
	public String toString()
	{
		return "StatisticItem{" +
				"icon='" + icon + '\'' +
				", text='" + text + '\'' +
				", backgroundColor=" + backgroundColor +
				", textColor=" + textColor +
				'}';
	}
}
