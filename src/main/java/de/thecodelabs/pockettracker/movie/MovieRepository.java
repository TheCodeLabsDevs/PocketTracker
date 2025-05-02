package de.thecodelabs.pockettracker.movie;

import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemRepository;
import de.thecodelabs.pockettracker.movie.model.Movie;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends BaseMediaItemRepository<Movie>
{
}
