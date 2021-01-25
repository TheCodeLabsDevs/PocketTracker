package de.thecodelabs.pockettracker.user;

public class StatisticItem
{
	private final String icon;
	private final String text;

	public StatisticItem(String icon, String text)
	{
		this.icon = icon;
		this.text = text;
	}

	public String getIcon()
	{
		return icon;
	}

	public String getText()
	{
		return text;
	}

	@Override
	public String toString()
	{
		return "StatisticItem{" +
				"icon='" + icon + '\'' +
				", text='" + text + '\'' +
				'}';
	}
}
