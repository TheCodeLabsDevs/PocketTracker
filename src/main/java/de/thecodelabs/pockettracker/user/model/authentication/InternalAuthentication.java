package de.thecodelabs.pockettracker.user.model.authentication;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

	public void setRememberMeDate(Date rememberMeDate)
	{
		if(rememberMeDate == null)
		{
			this.rememberMeDate = null;
		}
		else
		{
			this.rememberMeDate = rememberMeDate.toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalDateTime();
		}
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
			setRememberMeDate((Date) null);
			setRememberMeSeries(null);
			setRememberMeToken(null);
		}
		else
		{
			setRememberMeDate(token.getDate());
			setRememberMeSeries(token.getSeries());
			setRememberMeToken(token.getTokenValue());
		}
	}
}
