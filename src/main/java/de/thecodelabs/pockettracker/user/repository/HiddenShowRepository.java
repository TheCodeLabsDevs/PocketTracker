package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.HiddenShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


public interface HiddenShowRepository extends JpaRepository<HiddenShow, HiddenShow.HiddenShowId>
{
	@Transactional
	@Modifying
	@Query("DELETE FROM HiddenShow show WHERE show.showId=?1 and show.userId=?2")
	void deleteHiddenShow(@Param("showId") UUID showId, @Param("userId") UUID userId);
}
