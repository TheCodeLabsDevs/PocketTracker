package de.thecodelabs.pockettracker.backup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BackupSeasonModel
{
	private UUID id;

	private String name;

	@JsonProperty("desc")
	private String description;

	private Integer number;

	@MergeIgnore
	private List<BackupEpisodeModel> episodes;
	
	private Boolean filledCompletely;
}
