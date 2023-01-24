package de.thecodelabs.pockettracker.importer.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

import java.util.Objects;

public class ShowSearchRequest
{
	private APIType type;
	private String search;

	public ShowSearchRequest(APIType type, String search)
	{
		this.type = type;
		this.search = search;
	}

	public APIType getType()
	{
		return type;
	}

	public void setType(APIType type)
	{
		this.type = type;
	}

	public String getSearch()
	{
		return search;
	}

	public void setSearch(String search)
	{
		this.search = search;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ShowSearchRequest that = (ShowSearchRequest) o;
		return type == that.type && Objects.equals(search, that.search);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, search);
	}

	@Override
	public String toString()
	{
		return "ShowSearchRequest{" +
				"type=" + type +
				", search='" + search + '\'' +
				'}';
	}
}
