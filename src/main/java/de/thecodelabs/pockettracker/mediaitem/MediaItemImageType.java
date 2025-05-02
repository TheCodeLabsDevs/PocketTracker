package de.thecodelabs.pockettracker.mediaitem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MediaItemImageType
{
	BANNER("banner"),
	POSTER("poster");

	private final String pathName;
}
