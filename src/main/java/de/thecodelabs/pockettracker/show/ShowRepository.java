package de.thecodelabs.pockettracker.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer>
{
	List<Show> findAllByOrderByNameAsc();

	List<Show> findAllByNameContainingOrderByNameAsc(String name);
}
