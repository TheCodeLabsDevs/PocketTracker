package de.thecodelabs.pockettracker.importer.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

import java.util.Objects;

public class ShowSearchRequest
{
	private APIType apiIdentifierType;
	private String search;

	public ShowSearchRequest(APIType apiIdentifierType, String search)
	{
		this.apiIdentifierType = apiIdentifierType;
		this.search = search;
	}

	public APIType getApiIdentifierType()
	{
		return apiIdentifierType;
	}

	public void setApiIdentifierType(APIType apiIdentifierType)
	{
		this.apiIdentifierType = apiIdentifierType;
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
		return apiIdentifierType == that.apiIdentifierType && Objects.equals(search, that.search);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(apiIdentifierType, search);
	}

	@Override
	public String toString()
	{
		return "ShowSearchRequest{" +
				"apiIdentifierType=" + apiIdentifierType +
				", search='" + search + '\'' +
				'}';
	}
}
