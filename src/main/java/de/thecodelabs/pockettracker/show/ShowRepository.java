package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.show.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer>
{
	List<Show> findAllByOrderByNameAsc();

	List<Show> findAllByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name);
}
