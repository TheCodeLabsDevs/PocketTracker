package de.thecodelabs.pockettracker.episode.model;

import lombok.Getter;

@Getter
public enum EpisodeImageType
{
	POSTER("episodePoster");

	private final String pathName;

	EpisodeImageType(String pathName)
	{
		this.pathName = pathName;
	}

}
