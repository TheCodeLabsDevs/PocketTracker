package de.thecodelabs.pockettracker.mediaitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BaseMediaItemRepository<T extends MediaItem> extends JpaRepository<T, UUID>
{
	List<T> findAllByOrderByNameAsc();

	List<T> findAllByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name);
}
