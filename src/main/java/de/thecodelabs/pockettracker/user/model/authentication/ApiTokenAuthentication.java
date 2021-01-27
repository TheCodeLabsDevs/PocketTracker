package de.thecodelabs.pockettracker.user.model.authentication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ApiTokenAuthentication
{
	@Id
	@GeneratedValue
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
