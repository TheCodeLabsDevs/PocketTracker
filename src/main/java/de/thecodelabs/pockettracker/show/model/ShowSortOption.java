package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import lombok.Getter;
import org.springframework.context.MessageSourceResolvable;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public enum ShowSortOption implements MessageSourceResolvable
{
	NAME((shows, lastWatchDates) -> shows
			.sorted(Comparator.comparing(s -> s.getName().toLowerCase()))
			.toList()),
	LAST_WATCHED((shows, lastWatchDates) -> shows
			.sorted(Comparator.comparing((Show show) -> lastWatchDates.getOrDefault(show.getId(), LocalDate.MIN))
					.reversed()
					.thenComparing(Show::getName))
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
		List<Show> sort(Stream<Show> shows, Map<UUID, LocalDate> lastWatchDates);
	}

	public static LocalDate getLatestWatchDate(Show show, User user)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watchedEpisode -> watchedEpisode.getEpisode().getSeason().getShow().equals(show))
				.map(WatchedEpisode::getWatchedAt)
				.max(LocalDate::compareTo).orElse(LocalDate.MIN);
	}
}
