package de.thecodelabs.pockettracker.importer.model;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class MovieSearchItem
{
	private String name;
	private String releaseDate;
	private Integer identifier;
}
