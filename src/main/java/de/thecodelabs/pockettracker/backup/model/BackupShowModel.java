package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thecodelabs.pockettracker.show.model.ShowType;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BackupShowModel
{
	private UUID id;

	private String name;

	@JsonProperty("desc")
	private String description;

	@JsonProperty("first")
	private LocalDate firstAired;

	@JsonProperty("banner")
	private String bannerPath;

	@JsonProperty("poster")
	private String posterPath;

	private ShowType type;

	private Boolean finished;

	@MergeIgnore
	private List<BackupSeasonModel> seasons;

	@MergeIgnore
	private List<BackupAPIIdentifierModel> apiIdentifiers;
}
