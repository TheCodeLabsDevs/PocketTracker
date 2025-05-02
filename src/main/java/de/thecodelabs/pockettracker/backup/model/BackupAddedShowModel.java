package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BackupAddedShowModel
{
	@JsonProperty("show")
	private UUID showId;

	@JsonProperty("disliked")
	private Boolean disliked;
}
