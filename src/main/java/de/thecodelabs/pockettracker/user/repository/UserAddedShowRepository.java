package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.AddedShow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserAddedShowRepository extends JpaRepository<AddedShow, AddedShow.AddedShowId>
{
	@Transactional
	@Modifying
	@Query("DELETE FROM AddedShow show WHERE show.showId=?1 and show.userId=?2")
	void deleteAddedShow(Integer showId, Integer userId);
}
