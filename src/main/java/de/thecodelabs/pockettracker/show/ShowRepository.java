package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer>
{
	List<Show> findAllByOrderByNameAsc();

	List<Show> findAllByNameContainingIgnoreCaseOrderByNameAsc(String name);

	List<Show> findAllByFavoriteUsersContainingOrderByNameAsc(User user);

	List<Show> findAllByNameContainingIgnoreCaseAndFavoriteUsersContainingOrderByNameAsc(String name, User user);
}
