package de.thecodelabs.pockettracker.show.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeasonsDialogModel
{
	@NotNull
	@Min(0)
	private Integer seasonCount;
}
