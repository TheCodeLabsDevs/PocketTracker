package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemRepository;
import de.thecodelabs.pockettracker.show.model.Show;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends BaseMediaItemRepository<Show>
{
}
