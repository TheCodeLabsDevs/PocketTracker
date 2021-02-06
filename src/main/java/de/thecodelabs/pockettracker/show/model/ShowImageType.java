package de.thecodelabs.pockettracker.show.model;

public enum ShowImageType
{
	BANNER("banner"),
	POSTER("poster");

	private final String pathName;

	ShowImageType(String pathName)
	{
		this.pathName = pathName;
	}

	public String getPathName()
	{
		return pathName;
	}
}
