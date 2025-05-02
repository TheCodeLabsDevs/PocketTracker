package de.thecodelabs.pockettracker.administration.apiconfiguration.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@ToString
public class APIConfiguration
{
	public static class View
	{
		public interface Summary
		{
		}
	}

	@Id
	@JsonView(View.Summary.class)
	private UUID id;

	@Column(length = 4096)
	@NotEmpty
	@Size(max = 4096)
	@JsonView(View.Summary.class)
	private String token;

	@NotNull
	@JsonView(View.Summary.class)
	private APIType type;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}
}
