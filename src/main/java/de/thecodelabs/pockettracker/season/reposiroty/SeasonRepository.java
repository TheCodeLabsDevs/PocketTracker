package de.thecodelabs.pockettracker.season.reposiroty;

import de.thecodelabs.pockettracker.season.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer>
{

	List<Season> findAllByShowId(@Param("showId") Integer showId);
}
