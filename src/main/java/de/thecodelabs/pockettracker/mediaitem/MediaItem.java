package de.thecodelabs.pockettracker.mediaitem;

public interface MediaItem
{
	String getName();

	String getImagePath(ShowImageType showImageType);

	void setImagePath(ShowImageType showImageType, String path);
}
