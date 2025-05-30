package de.thecodelabs.pockettracker.importer.model;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class ShowSearchItem
{
	private String name;
	private String firstAired;
	private Integer identifier;
}
