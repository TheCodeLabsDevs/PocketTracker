package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BackupAddedMovieModel
{
	@JsonProperty("movie")
	private UUID movieId;

	private LocalDate watchedDate;
}
