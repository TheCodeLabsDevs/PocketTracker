package de.thecodelabs.pockettracker.utils;

import de.thecodelabs.pockettracker.episode.model.Episode;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class Helpers
{
	private Helpers()
	{
	}

	public static String getShortCode(Episode episode)
	{
		return MessageFormat.format("[S{0} E{1}]",
				String.format("%02d", episode.getSeason().getNumber()),
				String.format("%02d", episode.getNumber()));
	}

	public static String replaceNonAlphaNumericCharacters(String text, String replacement)
	{
		return text.replaceAll("[^A-Za-z0-9\\.\\+]", replacement);
	}
}
