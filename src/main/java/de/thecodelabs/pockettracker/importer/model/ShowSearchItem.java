package de.thecodelabs.pockettracker.importer.model;

import java.util.Objects;

public class ShowSearchItem
{
	private String name;
	private String firstAired;
	private Integer identifier;

	public ShowSearchItem(String name, String firstAired, Integer identifier)
	{
		this.name = name;
		this.firstAired = firstAired;
		this.identifier = identifier;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getFirstAired()
	{
		return firstAired;
	}

	public void setFirstAired(String firstAired)
	{
		this.firstAired = firstAired;
	}

	public Integer getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(Integer identifier)
	{
		this.identifier = identifier;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ShowSearchItem that = (ShowSearchItem) o;
		return Objects.equals(name, that.name) && Objects.equals(firstAired, that.firstAired) && Objects.equals(identifier, that.identifier);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, firstAired, identifier);
	}

	@Override
	public String toString()
	{
		return "ShowSearchItem{" +
				"name='" + name + '\'' +
				", firstAired='" + firstAired + '\'' +
				", identifier=" + identifier +
				'}';
	}
}
