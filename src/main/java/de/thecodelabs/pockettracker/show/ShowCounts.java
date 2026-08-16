package de.thecodelabs.pockettracker.show;

import java.util.UUID;

public interface ShowCounts
{
	UUID getShowId();

	long getSeasonCount();

	long getEpisodeCount();
}
