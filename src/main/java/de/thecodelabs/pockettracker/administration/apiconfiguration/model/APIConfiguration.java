package de.thecodelabs.pockettracker.administration.apiconfiguration.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

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
	@GeneratedValue(generator = "custom_generator")
	@GenericGenerator(name = "custom_generator", strategy = "de.thecodelabs.pockettracker.utils.CustomIdGenerator")
	@JsonView(View.Summary.class)
	private Integer id;

	@Column(length = 4096)
	@NotEmpty
	@Size(max = 4096)
	@JsonView(View.Summary.class)
	private String token;

	@NotNull
	@JsonView(View.Summary.class)
	private APIType type;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
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

