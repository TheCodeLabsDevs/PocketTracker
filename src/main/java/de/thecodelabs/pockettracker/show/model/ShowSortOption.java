package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import org.springframework.context.MessageSourceResolvable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ShowSortOption implements MessageSourceResolvable
{
	NAME((shows, user) -> shows.stream()
			.sorted(Comparator.comparing(Show::getName))
			.collect(Collectors.toList())),
	LAST_WATCHED((shows, user) -> shows.stream()
			.sorted(Comparator.comparing((Show show) -> getLatestWatchDate(show, user))
					.thenComparing(Show::getName)
					.reversed())
			.collect(Collectors.toList())),
	RUNNING((shows, user) -> shows.stream()
			.filter(show -> !Optional.ofNullable(show.getFinished()).orElse(false))
			.sorted(Comparator.comparing(Show::getName))
			.collect(Collectors.toList()));

	private final ShowSort sorter;

	ShowSortOption(ShowSort sorter)
	{
		this.sorter = sorter;
	}

	public ShowSort getSorter()
	{
		return sorter;
	}

	@Override
	public String[] getCodes()
	{
		return new String[]{"ShowSortOption." + name()};
	}

	public interface ShowSort
	{
		List<Show> sort(List<Show> shows, User user);
	}

	private static LocalDate getLatestWatchDate(Show show, User user)
	{
		return show.getSeasons().stream()
				.flatMap(season -> season.getEpisodes().stream())
				.flatMap(episode -> episode.getWatchedEpisodes().stream())
				.filter(watchedEpisode -> watchedEpisode.getUser().equals(user))
				.map(WatchedEpisode::getWatchedAt)
				.max(LocalDate::compareTo).orElse(null);
	}
}
