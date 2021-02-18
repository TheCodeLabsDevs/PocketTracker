package de.thecodelabs.pockettracker.user.model.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.MessageSourceResolvable;

import javax.persistence.*;

@Entity
@Table(name = "appuser_authentication")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class UserAuthentication implements MessageSourceResolvable
{
	@Id
	@GeneratedValue(generator = "custom_generator")
	@GenericGenerator(name = "custom_generator", strategy = "de.thecodelabs.pockettracker.utils.CustomIdGenerator")
	private Integer id;

	@ManyToOne
	private User user;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
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
