package de.thecodelabs.pockettracker.season.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EpisodesDialogModel
{
	@NotNull
	@Min(0)
	private Integer episodeCount;
}