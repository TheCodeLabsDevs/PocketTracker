package de.thecodelabs.pockettracker.mediaitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@NoRepositoryBean
public interface BaseMediaItemRepository<T extends MediaItem> extends JpaRepository<T, UUID>
{
	List<T> findAllByOrderByNameAsc();

	List<T> findAllByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name);
}
