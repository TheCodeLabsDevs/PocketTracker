package de.thecodelabs.pockettracker.movie.model;

public enum MovieImageType
{
	POSTER("movie_poster");

	private final String pathName;

	MovieImageType(String pathName)
	{
		this.pathName = pathName;
	}

	public String getPathName()
	{
		return pathName;
	}
}
