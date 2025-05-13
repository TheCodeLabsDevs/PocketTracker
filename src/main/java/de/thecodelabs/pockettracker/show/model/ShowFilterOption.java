package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.user.model.User;
import lombok.Getter;
import org.springframework.context.MessageSourceResolvable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum ShowFilterOption implements MessageSourceResolvable
{
	ALL_SHOWS((shows, user) -> shows.stream(), 0),
	VIEWED_COMPLETELY((shows, user) -> shows.stream()
			.filter(show -> isFavoriteShow(show, user))
			.filter(show -> isShowComplete(show, user)), 3),
	NOT_VIEWED_COMPLETELY((shows, user) -> shows.stream()
			.filter(show -> isFavoriteShow(show, user))
			.filter(show -> !isShowComplete(show, user)), 4),
	RUNNING((shows, user) -> shows.stream().filter(show -> !Optional.ofNullable(show.getFinished()).orElse(false)), 6),
	NOT_RUNNING((shows, user) -> shows.stream().filter(show -> Optional.ofNullable(show.getFinished()).orElse(false)), 7),
	NOT_FILLED_COMPLETELY((shows, user) -> shows.stream().filter(show -> show.getSeasons().stream().anyMatch(season -> !season.getFilledCompletely())), 8),
	FAVORITE((shows, user) -> shows.stream()
			.filter(show -> isFavoriteShow(show, user)), 1),
	NOT_FAVORITE((shows, user) -> shows.stream()
			.filter(show -> !isFavoriteShow(show, user)), 2),
	NEVER_VIEWED((shows, user) -> shows.stream()
			.filter(show -> !isFavoriteShow(show, user))
			.filter(show -> getNumberOfWatchedEpisodes(show, user) == 0), 5);


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
		Stream<Show> filter(List<Show> shows, User user);
	}

	public static boolean isShowComplete(Show show, User user)
	{
		final long watchedEpisodes = getNumberOfWatchedEpisodes(show, user);

		long totalEpisodes = show.getSeasons().stream()
				.mapToLong(season -> season.getEpisodes().size())
				.sum();

		return watchedEpisodes == totalEpisodes;
	}

	private static long getNumberOfWatchedEpisodes(Show show, User user)
	{
		return user.getWatchedEpisodes().stream()
				.filter(watchedEpisode -> watchedEpisode.getEpisode().getSeason().getShow().equals(show))
				.count();
	}

	public static boolean isFavoriteShow(Show show, User user)
	{
		return user.getShowById(show.getId()).isPresent();
	}
}
