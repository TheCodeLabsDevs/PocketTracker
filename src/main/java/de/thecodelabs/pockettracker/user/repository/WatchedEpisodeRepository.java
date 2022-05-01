package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface WatchedEpisodeRepository extends JpaRepository<WatchedEpisode, WatchedEpisode.WatchedEpisodeId>
{
	@Transactional
	@Modifying
	@Query("DELETE FROM WatchedEpisode episode WHERE episode.episodeId = ?1 and episode.userId = ?2")
	void deleteWatchedEpisode(@Param("episodeId") Integer episodeId, @Param("userId") Integer userId);
}
