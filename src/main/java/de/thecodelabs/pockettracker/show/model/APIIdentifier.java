package de.thecodelabs.pockettracker.show.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class APIIdentifier
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

	@NotNull
	@JsonView(View.Summary.class)
	private APIType type;

	@Column(length = 4096)
	@Size(max = 4096)
	@NotEmpty
	@JsonView(View.Summary.class)
	private String identifier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@MergeIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Show show;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@MergeIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Movie movie;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

	public APIIdentifier(APIType type, String identifier, Show show)
	{
		this.type = type;
		this.identifier = identifier;
		this.show = show;
	}

	public APIIdentifier(APIType type, String identifier, Movie movie)
	{
		this.type = type;
		this.identifier = identifier;
		this.movie = movie;
	}

	public APIIdentifier(UUID id, APIType type, String identifier)
	{
		this.id = id;
		this.type = type;
		this.identifier = identifier;
	}
}
