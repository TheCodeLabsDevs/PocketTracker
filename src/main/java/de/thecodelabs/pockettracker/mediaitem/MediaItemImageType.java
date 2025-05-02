package de.thecodelabs.pockettracker.mediaitem;

public enum MediaItemImageType
{
	BANNER("banner"),
	POSTER("poster");

	private final String pathName;

	MediaItemImageType(String pathName)
	{
		this.pathName = pathName;
	}

	public String getPathName()
	{
		return pathName;
	}
}
