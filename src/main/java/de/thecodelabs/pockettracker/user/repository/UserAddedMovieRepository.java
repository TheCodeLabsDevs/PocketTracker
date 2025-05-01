package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.AddedShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


public interface UserAddedMovieRepository extends JpaRepository<AddedMovie, AddedMovie.AddedMovieId>
{
	@Transactional
	@Modifying
	@Query("DELETE FROM AddedMovie movie WHERE movie.movieId=?1 and movie.userId=?2")
	void deleteAddedMovie(@Param("movieId") UUID movieId, @Param("userId") UUID userId);
}
