package de.thecodelabs.pockettracker.user.model.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import jakarta.persistence.*;
import org.springframework.context.MessageSourceResolvable;

import java.util.UUID;

@Entity
@Table(name = "appuser_authentication")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class UserAuthentication implements MessageSourceResolvable
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	private User user;

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@Override
	public String[] getCodes()
	{
		final String providerName = getType();
		return new String[]{"authentication.provider." + providerName};
	}

	public String getType()
	{
		return getClass().getAnnotation(DiscriminatorValue.class).value();
	}
}
