package de.thecodelabs.pockettracker.mediaitem;

public interface MediaItem
{
	String getName();

	String getImagePath(MediaItemImageType mediaItemImageType);

	void setImagePath(MediaItemImageType mediaItemImageType, String path);
}
