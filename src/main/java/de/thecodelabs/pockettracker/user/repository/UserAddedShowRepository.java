package de.thecodelabs.pockettracker.user.repository;

import de.thecodelabs.pockettracker.user.model.AddedShow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddedShowRepository extends JpaRepository<AddedShow, AddedShow.AddedShowId>
{
}
