package de.thecodelabs.pockettracker.show.model;

import de.thecodelabs.pockettracker.user.model.User;
import org.springframework.context.MessageSourceResolvable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public enum ShowFilterOption implements MessageSourceResolvable
{
	ALL_SHOWS((shows, user) -> shows.stream()),
	VIEWED_COMPLETELY((shows, user) -> shows.stream().filter(show -> isShowComplete(show, user))),
	NOT_VIEWED_COMPLETELY((shows, user) -> shows.stream().filter(show -> !isShowComplete(show, user))),
	RUNNING((shows, user) -> shows.stream().filter(show -> !Optional.ofNullable(show.getFinished()).orElse(false))),
	NOT_RUNNING((shows, user) -> shows.stream().filter(show -> Optional.ofNullable(show.getFinished()).orElse(false)));

	private final ShowFilter filter;

	ShowFilterOption(ShowFilter filter)
	{
		this.filter = filter;
	}

	public ShowFilter getFilter()
	{
		return filter;
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
		long watchedEpisodes = user.getWatchedEpisodes().stream()
				.filter(watchedEpisode -> watchedEpisode.getEpisode().getSeason().getShow().equals(show))
				.count();

		long totalEpisodes = show.getSeasons().stream()
				.mapToLong(season -> season.getEpisodes().size())
				.sum();

		return watchedEpisodes == totalEpisodes;
	}
}
