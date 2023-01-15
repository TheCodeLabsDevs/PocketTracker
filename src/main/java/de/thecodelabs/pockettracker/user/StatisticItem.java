package de.thecodelabs.pockettracker.user;

import de.thecodelabs.pockettracker.utils.BootstrapColor;


public record StatisticItem(String icon, String text, BootstrapColor backgroundColor, BootstrapColor textColor)
{
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
