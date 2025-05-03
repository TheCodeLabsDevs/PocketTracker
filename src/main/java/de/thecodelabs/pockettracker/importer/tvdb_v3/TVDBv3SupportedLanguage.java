package de.thecodelabs.pockettracker.importer.tvdb_v3;

import lombok.Getter;

@Getter
public enum TVDBv3SupportedLanguage
{
	ENGLISH("en", "eng"),
	GERMAN("de", "deu");

	private final String key;
	private final String longKey;

	TVDBv3SupportedLanguage(String key, String longKey)
	{
		this.key = key;
		this.longKey = longKey;
	}

	static TVDBv3SupportedLanguage fromKey(String key)
	{
		return switch(key)
		{
			case "de" -> GERMAN;
			case "en" -> ENGLISH;
			default -> throw new IllegalArgumentException("Invalid key: " + key);
		};
	}
}
