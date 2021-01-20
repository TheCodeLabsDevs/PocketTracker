package de.thecodelabs.pockettracker.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer>
{
	public List<Show> findAllByOrderByNameAsc();
}
