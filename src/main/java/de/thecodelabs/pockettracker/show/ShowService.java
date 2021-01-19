package de.thecodelabs.pockettracker.show;

import org.springframework.stereotype.Service;

@Service
public class ShowService
{
	public Integer getTotalNumberOfEpisodes(Show show)
	{
		return show.getSeasons().stream().mapToInt(season -> season.getEpisodes().size()).sum();
	}
}
