package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.user.model.User;
import lombok.Getter;
import org.springframework.context.MessageSourceResolvable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public enum ShowFilterOption implements MessageSourceResolvable
{
	ALL_SHOWS((shows, user, watchedCounts, episodeCounts, incompleteSeasonShowIds) -> shows.stream(), 0),
	VIEWED_COMPLETELY((shows, user, watchedCounts, episodeCounts, incompleteSeasonShowIds) -> shows.stream()
			.filter(show -> isShowComplete(show, watchedCounts, episodeCounts)), 1),
	NOT_VIEWED_COMPLETELY((shows, user, watchedCounts, episodeCounts, incompleteSeasonShowIds) -> shows.stream()
			.filter(show -> !isShowComplete(show, watchedCounts, episodeCounts))
			.filter(show -> getNumberOfWatchedEpisodes(show, watchedCounts) > 0), 2),
	RUNNING((shows, user, watchedCounts, episodeCounts, incompleteSeasonShowIds) -> shows.stream().filter(show -> !Optional.ofNullable(show.getFinished()).orElse(false)), 4),
	NOT_RUNNING((shows, user, watchedCounts, episodeCounts, incompleteSeasonShowIds) -> shows.stream().filter(show -> Optional.ofNullable(show.getFinished()).orElse(false)), 5),
	NOT_FILLED_COMPLETELY((shows, user, watchedCounts, episodeCounts, incompleteSeasonShowIds) -> shows.stream().filter(show -> incompleteSeasonShowIds.contains(show.getId())), 6),
	NEVER_VIEWED((shows, user, watchedCounts, episodeCounts, incompleteSeasonShowIds) -> shows.stream()
			.filter(show -> getNumberOfWatchedEpisodes(show, watchedCounts) == 0), 3);

	private final ShowFilter filter;
	private final int order;

	ShowFilterOption(ShowFilter filter, int order)
	{
		this.filter = filter;
		this.order = order;
	}

	@Override
	public String[] getCodes()
	{
		return new String[]{"ShowFilterOption." + name()};
	}

	public interface ShowFilter
	{
		Stream<Show> filter(List<Show> shows, User user, Map<UUID, Long> watchedCounts, Map<UUID, Long> episodeCounts, Set<UUID> incompleteSeasonShowIds);
	}

	public static boolean isShowComplete(Show show, Map<UUID, Long> watchedCounts, Map<UUID, Long> episodeCounts)
	{
		final long watchedEpisodes = watchedCounts.getOrDefault(show.getId(), 0L);
		final long totalEpisodes = episodeCounts.getOrDefault(show.getId(), 0L);

		return watchedEpisodes == totalEpisodes;
	}

	private static long getNumberOfWatchedEpisodes(Show show, Map<UUID, Long> watchedCounts)
	{
		return watchedCounts.getOrDefault(show.getId(), 0L);
	}
}
