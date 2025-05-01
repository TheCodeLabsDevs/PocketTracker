package de.thecodelabs.pockettracker.importer.tvdb_v3;

public enum TVDBv3SupportedLanguage
{
	ENGLISH("en"),
	GERMAN("de");

	private final String key;

	TVDBv3SupportedLanguage(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}
}
