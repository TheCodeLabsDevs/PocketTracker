package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemRepository;
import de.thecodelabs.pockettracker.show.model.Show;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShowRepository extends BaseMediaItemRepository<Show>
{
	@Query("""
			SELECT s
			FROM Show s
			WHERE (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
			ORDER BY s.name ASC
			""")
	List<Show> findAllForOverview(@Nullable @Param("search") String search);

	@Query("""
			SELECT s.id AS showId,
				   COUNT(DISTINCT se.id) AS seasonCount,
				   COUNT(e) AS episodeCount
			FROM Show s
			LEFT JOIN s.seasons se
			LEFT JOIN se.episodes e
			GROUP BY s.id
			""")
	List<ShowCounts> findSeasonAndEpisodeCounts();

	@Query("""
			SELECT DISTINCT s.id
			FROM Show s
			JOIN s.seasons se
			WHERE se.filledCompletely = FALSE
			""")
	List<UUID> findIdsWithIncompleteSeason();
}
