package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import lombok.Getter;
import org.springframework.context.MessageSourceResolvable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Getter
public enum ShowSortOption implements MessageSourceResolvable
{
	NAME((shows, user) -> shows
			.sorted(Comparator.comparing(s -> s.getName().toLowerCase()))
			.toList()),
	LAST_WATCHED((shows, user) -> shows
			.sorted(Comparator.comparing((Show show) -> getLatestWatchDate(show, user))
					.thenComparing(Show::getName)
					.reversed())
			.toList());

	private final ShowSort sorter;

	ShowSortOption(ShowSort sorter)
	{
		this.sorter = sorter;
	}

	@Override
	public String[] getCodes()
	{
		return new String[]{"ShowSortOption." + name()};
	}

	public interface ShowSort
	{
		List<Show> sort(Stream<Show> shows, User user);
	}

	public static LocalDate getLatestWatchDate(Show show, User user)
	{
		return show.getSeasons().stream()
				.flatMap(season -> season.getEpisodes().stream())
				.flatMap(episode -> episode.getWatchedEpisodes().stream())
				.filter(watchedEpisode -> watchedEpisode.getUser().equals(user))
				.map(WatchedEpisode::getWatchedAt)
				.max(LocalDate::compareTo).orElse(LocalDate.MIN);
	}
}
