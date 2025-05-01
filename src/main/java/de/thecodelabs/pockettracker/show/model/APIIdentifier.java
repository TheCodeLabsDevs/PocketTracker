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

import java.util.Objects;
import java.util.UUID;

@Entity
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
	private Show show;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@MergeIgnore
	private Movie movie;

	@PrePersist
	void prePersists()
	{
		if(id == null)
		{
			id = UUID.randomUUID();
		}
	}

	public APIIdentifier()
	{
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

	public void setId(UUID id)
	{
		this.id = id;
	}

	public UUID getId()
	{
		return id;
	}

	public APIType getType()
	{
		return type;
	}

	public void setType(APIType type)
	{
		this.type = type;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public Show getShow()
	{
		return show;
	}

	public void setShow(Show show)
	{
		this.show = show;
	}

	public Movie getMovie()
	{
		return movie;
	}

	public void setMovie(Movie movie)
	{
		this.movie = movie;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		APIIdentifier that = (APIIdentifier) o;
		return Objects.equals(id, that.id) && type == that.type && Objects.equals(identifier, that.identifier);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, type, identifier);
	}

	@Override
	public String toString()
	{
		return "APIIdentifier{" +
				"id=" + id +
				", type=" + type +
				", identifier='" + identifier + '\'' +
				'}';
	}
}
