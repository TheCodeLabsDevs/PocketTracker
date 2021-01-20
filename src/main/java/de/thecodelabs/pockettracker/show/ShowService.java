package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.episode.Episode;
import org.springframework.stereotype.Service;

@Service
public class ShowService
{
	public Integer getTotalNumberOfEpisodes(Show show)
	{
		return show.getSeasons().stream()
				.mapToInt(season -> season.getEpisodes().size())
				.sum();
	}

	public Integer getTotalPlayTime(Show show)
	{
		return show.getSeasons().stream()
				.flatMapToInt(season -> season.getEpisodes().stream()
						.mapToInt(Episode::getLengthInMinutes))
				.sum();
	}
}
