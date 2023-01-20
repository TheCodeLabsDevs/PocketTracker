package de.thecodelabs.pockettracker.administration.apiconfiguration.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@JsonView(View.Summary.class)
	private String name = "";

	@Column(length = 4096)
	@NotEmpty
	@Size(max = 4096)
	@JsonView(View.Summary.class)
	private String token;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		APIConfiguration that = (APIConfiguration) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(token, that.token);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name, token);
	}

	@Override
	public String toString()
	{
		return "APIConfiguration{" +
				"id=" + id +
				", name='" + name + '\'' +
				", token='" + token + '\'' +
				'}';
	}
}

