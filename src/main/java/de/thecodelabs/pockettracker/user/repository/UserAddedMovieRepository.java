package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.AddedMovie;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserAddedMovieRepository extends JpaRepository<AddedMovie, AddedMovie.AddedMovieId>
{
}
