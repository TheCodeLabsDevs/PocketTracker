package de.thecodelabs.pockettracker.backup.model.user;

import java.time.LocalDateTime;

public class BackupUserTokenModel
{
	private Integer id;
	private LocalDateTime createDate;
	private String token;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
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
