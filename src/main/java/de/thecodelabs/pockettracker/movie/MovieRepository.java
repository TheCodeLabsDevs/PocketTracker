package de.thecodelabs.pockettracker.movie;

import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemRepository;
import de.thecodelabs.pockettracker.movie.model.Movie;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends BaseMediaItemRepository<Movie>
{
	List<Movie> findAllByOrderByNameAsc();

	List<Movie> findAllByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name);
}
