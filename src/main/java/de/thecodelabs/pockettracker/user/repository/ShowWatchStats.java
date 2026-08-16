package de.thecodelabs.pockettracker.user.repository;

import java.time.LocalDate;
import java.util.UUID;

public interface ShowWatchStats
{
	UUID getShowId();

	LocalDate getLastWatched();

	long getWatchedCount();
}
