package de.thecodelabs.pockettracker.mediaitem;

import de.thecodelabs.pockettracker.show.model.ShowImageType;

public interface MediaItem
{
	String getName();

	String getImagePath(ShowImageType showImageType);

	void setImagePath(ShowImageType showImageType, String path);
}
