package de.thecodelabs.pockettracker.episode.repository;

import de.thecodelabs.pockettracker.episode.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer>
{
}
