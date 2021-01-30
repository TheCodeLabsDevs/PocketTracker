package de.thecodelabs.pockettracker.user.model.authentication;

import de.thecodelabs.pockettracker.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appuser_token")
public class ApiTokenAuthentication
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDateTime createDate;
	private String token;

	@ManyToOne(cascade = CascadeType.ALL)
	private User user;

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

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
}
