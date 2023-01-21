package de.thecodelabs.pockettracker.show.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;
import de.thecodelabs.pockettracker.utils.beans.MergeIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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
	@GeneratedValue(generator = "custom_generator")
	@GenericGenerator(name = "custom_generator", strategy = "de.thecodelabs.pockettracker.utils.CustomIdGenerator")
	@JsonView(View.Summary.class)
	private Integer id;

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

	public APIIdentifier()
	{
	}

	public APIIdentifier(Integer id, APIType type, String identifier)
	{
		this.id = id;
		this.type = type;
		this.identifier = identifier;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
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
