package de.thecodelabs.pockettracker.backup.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BackupAPIIdentifierModel
{
	private UUID id;
	private APIType type;
	private String identifier;
}
