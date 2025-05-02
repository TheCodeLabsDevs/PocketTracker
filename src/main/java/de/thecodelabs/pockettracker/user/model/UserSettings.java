package de.thecodelabs.pockettracker.user.model;


import de.thecodelabs.pockettracker.show.model.ShowFilterOption;
import de.thecodelabs.pockettracker.show.model.ShowSortOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "appuser_settings")
@NoArgsConstructor
public class UserSettings
{
	@Id
	private UUID id;

	private Boolean showDislikedShows = false;
	private ShowSortOption lastShowSortOption = ShowSortOption.LAST_WATCHED;
	private ShowFilterOption lastShowFilterOption = ShowFilterOption.ALL_SHOWS;

	@OneToOne(mappedBy = "settings")
	private User user;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

	public UserSettings(User user)
	{
		this.user = user;
	}
}
