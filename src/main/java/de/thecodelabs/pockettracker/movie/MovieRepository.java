package de.thecodelabs.pockettracker.movie;

import de.thecodelabs.pockettracker.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID>
{
	List<Movie> findAllByOrderByNameAsc();

	List<Movie> findAllByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name);
}
