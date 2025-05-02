package de.thecodelabs.pockettracker.user.model.authentication;

import de.thecodelabs.pockettracker.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "appuser_token")
public class ApiTokenAuthentication
{
	@Id
	private UUID id;

	private LocalDateTime createDate;
	private String token;

	@ManyToOne(cascade = CascadeType.ALL)
	private User user;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}
}
