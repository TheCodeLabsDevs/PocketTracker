package de.thecodelabs.pockettracker.importer.tvdb_v3.converter;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.season.model.Season;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TVDBEpisodeToEpisodeConverter
{
	public Episode toEpisode(com.uwetrottmann.thetvdb.entities.Episode tvdbEpisode, Season season)
	{
		return new Episode(
				tvdbEpisode.episodeName,
				tvdbEpisode.overview,
				tvdbEpisode.airedEpisodeNumber,
				parseDate(tvdbEpisode.firstAired),
				null,
				season,
				null
		);
	}

	@Nullable
	private static LocalDate parseDate(String dateString)
	{
		if(dateString == null)
		{
			return null;
		}
		return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
	}
}
