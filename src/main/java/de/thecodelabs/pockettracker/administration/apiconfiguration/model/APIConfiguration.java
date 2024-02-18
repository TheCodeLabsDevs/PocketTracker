package de.thecodelabs.pockettracker.administration.apiconfiguration.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

@Entity
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

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public APIType getType()
	{
		return type;
	}

	public void setType(APIType type)
	{
		this.type = type;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		APIConfiguration that = (APIConfiguration) o;
		return Objects.equals(id, that.id) && Objects.equals(token, that.token) && type == that.type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, token, type);
	}

	@Override
	public String toString()
	{
		return "APIConfiguration{" +
				"id=" + id +
				", token='" + token + '\'' +
				", type=" + type +
				'}';
	}
}

