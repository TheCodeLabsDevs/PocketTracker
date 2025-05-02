package de.thecodelabs.pockettracker.importer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
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
}
