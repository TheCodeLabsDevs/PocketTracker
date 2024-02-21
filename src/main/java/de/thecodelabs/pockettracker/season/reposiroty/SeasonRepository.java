package de.thecodelabs.pockettracker.season.reposiroty;

import de.thecodelabs.pockettracker.season.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeasonRepository extends JpaRepository<Season, UUID>
{

	List<Season> findAllByShowId(@Param("showId") UUID showId);
}
