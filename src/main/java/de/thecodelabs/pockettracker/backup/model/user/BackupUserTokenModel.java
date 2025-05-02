package de.thecodelabs.pockettracker.backup.model.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class BackupUserTokenModel
{
	private UUID id;
	private LocalDateTime createDate;
	private String token;
}
