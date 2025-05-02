package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BackupWatchedEpisodeModel
{
	@JsonProperty("episode")
	private UUID episodeId;

	@JsonProperty("at")
	private LocalDate watchedAt;
}
