package de.thecodelabs.pockettracker.movie.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateMoveFromApiDialogModel
{
	private APIType apiType;
}
