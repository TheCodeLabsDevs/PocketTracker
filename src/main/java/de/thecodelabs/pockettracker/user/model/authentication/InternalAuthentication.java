package de.thecodelabs.pockettracker.user.model.authentication;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@DiscriminatorValue("internal")
public class InternalAuthentication extends UserAuthentication
{
	private String password;

	private LocalDateTime rememberMeDate;
	private String rememberMeToken;
	private String rememberMeSeries;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public LocalDateTime getRememberMeDate()
	{
		return rememberMeDate;
	}

	public void setRememberMeDate(LocalDateTime rememberMeDate)
	{
		this.rememberMeDate = rememberMeDate;
	}

	public String getRememberMeToken()
	{
		return rememberMeToken;
	}

	public void setRememberMeToken(String rememberMeToken)
	{
		this.rememberMeToken = rememberMeToken;
	}

	public String getRememberMeSeries()
	{
		return rememberMeSeries;
	}

	public void setRememberMeSeries(String rememberMeSeries)
	{
		this.rememberMeSeries = rememberMeSeries;
	}

	public void setRememberMe(PersistentRememberMeToken token)
	{
		if(token == null)
		{
			setRememberMeDate((LocalDateTime) null);
			setRememberMeSeries(null);
			setRememberMeToken(null);
		}
		else
		{
			setRememberMeDate(token.getDate().toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime());
			setRememberMeSeries(token.getSeries());
			setRememberMeToken(token.getTokenValue());
		}
	}
}
