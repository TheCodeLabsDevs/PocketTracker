package de.thecodelabs.pockettracker.episode.model;

public enum EpisodeImageType
{
	POSTER("episodePoster");

	private final String pathName;

	EpisodeImageType(String pathName)
	{
		this.pathName = pathName;
	}

	public String getPathName()
	{
		return pathName;
	}
}
