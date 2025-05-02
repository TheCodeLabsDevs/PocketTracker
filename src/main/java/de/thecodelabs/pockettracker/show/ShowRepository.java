package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemRepository;
import de.thecodelabs.pockettracker.show.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShowRepository extends BaseMediaItemRepository<Show>
{
	List<Show> findAllByOrderByNameAsc();

	List<Show> findAllByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name);
}
