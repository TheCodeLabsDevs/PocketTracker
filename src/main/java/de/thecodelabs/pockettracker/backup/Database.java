package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.season.Season;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.user.model.User;

import java.util.List;

public class Database
{
	private final List<Show> shows;
	private final List<Season> seasons;
	private final List<Episode> episodes;
	private final List<User> users;

	public Database(List<Show> shows, List<Season> seasons, List<Episode> episodes, List<User> users)
	{
		this.shows = shows;
		this.seasons = seasons;
		this.episodes = episodes;
		this.users = users;
	}

	public List<Show> getShows()
	{
		return shows;
	}

	public List<Season> getSeasons()
	{
		return seasons;
	}

	public List<Episode> getEpisodes()
	{
		return episodes;
	}

	public List<User> getUsers()
	{
		return users;
	}
}
