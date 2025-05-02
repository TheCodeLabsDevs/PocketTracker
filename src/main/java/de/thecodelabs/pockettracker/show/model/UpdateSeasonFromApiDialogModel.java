package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateSeasonFromApiDialogModel
{
	private APIType apiType;
}
