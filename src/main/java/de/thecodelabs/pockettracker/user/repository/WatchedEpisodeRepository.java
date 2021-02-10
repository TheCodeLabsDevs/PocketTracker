package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchedEpisodeRepository extends JpaRepository<WatchedEpisode, WatchedEpisode.WatchedEpisodeId>
{
}
