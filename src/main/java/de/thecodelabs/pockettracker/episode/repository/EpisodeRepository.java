package de.thecodelabs.pockettracker.episode.repository;

import de.thecodelabs.pockettracker.episode.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, UUID>
{
	Optional<Episode> findByNumberAndSeasonNumberAndSeasonShowId(@Param("episodeNumber") Integer episodeNumber, @Param("seasonNumber") Integer seasonNumber, @Param("showId") UUID showId);
}
