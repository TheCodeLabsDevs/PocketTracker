package de.thecodelabs.pockettracker.backup.model.user;

import java.time.LocalDateTime;
import java.util.UUID;

public class BackupUserTokenModel
{
	private UUID id;
	private LocalDateTime createDate;
	private String token;

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public LocalDateTime getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate)
	{
		this.createDate = createDate;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
}
