package de.thecodelabs.pockettracker.user.model.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSourceResolvable;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "appuser_authentication")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class UserAuthentication implements MessageSourceResolvable
{
	@Id
	private UUID id;

	@ManyToOne
	private User user;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
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
