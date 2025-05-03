package de.thecodelabs.pockettracker.importer.model;

import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class MovieSearchItem
{
	private String name;
	private String description;
	private String releaseDate;
	private Integer identifier;
	private Integer lengthInMinutes;

	@Nullable
	public LocalDate getParsedDate()
	{
		if(releaseDate == null)
		{
			return null;
		}
		return LocalDate.parse(releaseDate, DateTimeFormatter.ISO_DATE);
	}
}
