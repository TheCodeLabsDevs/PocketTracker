package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BackupMovieModel
{
	private UUID id;

	private String name;

	@JsonProperty("desc")
	private String description;

	@JsonProperty("first")
	private LocalDate releaseDate;

	@JsonProperty("poster")
	private String posterPath;

	@JsonProperty("duration")
	private Integer lengthInMinutes;

	@MergeIgnore
	private List<BackupAPIIdentifierModel> apiIdentifiers;
}
