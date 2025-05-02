package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BackupEpisodeModel
{
	private UUID id;

	private String name;

	@JsonProperty("desc")
	private String description;

	private Integer number;

	@JsonProperty("first")
	private LocalDate firstAired;

	@JsonProperty("min")
	private Integer lengthInMinutes;

	@JsonProperty("poster")
	private String posterPath;
}
