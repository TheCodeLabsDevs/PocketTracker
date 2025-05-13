package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemService;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.model.HiddenShow;
import de.thecodelabs.pockettracker.user.model.User;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShowService extends BaseMediaItemService<Show>
{
	private final MessageSource messageSource;

	public ShowService(ShowRepository repository, WebConfigurationProperties webConfigurationProperties, MessageSource messageSource)
	{
		super(repository, webConfigurationProperties);
		this.messageSource = messageSource;
	}

	public List<Show> getAllFavoriteByUser(String name, User user)
	{
		final List<Show> shows;
		if(name == null || name.isEmpty())
		{
			shows = user.getHiddenShows()
					.stream()
					.map(HiddenShow::getShow)
					.toList();
		}
		else
		{
			shows = user.getHiddenShows()
					.stream()
					.map(HiddenShow::getShow)
					.filter(show -> show.getName().toLowerCase().contains(name.toLowerCase()))
					.toList();
		}
		shows.forEach(this::prepareItem);
		return sort(shows);
	}

	@Override
	protected void prepareItem(Show item)
	{
		item.getSeasons().sort(Comparator.comparingInt(Season::getNumber));
	}

	@Transactional
	public Season addSeasonToShow(UUID showId)
	{
		final Optional<Show> showOptional = getById(showId);
		if(showOptional.isEmpty())
		{
			throw new NotFoundException("Show not found");
		}

		final Show show = showOptional.get();
		final int highestSeasonNumber = show.getSeasons().stream().mapToInt(Season::getNumber).max().orElse(0);

		final String title = messageSource.getMessage("season.defaultName", new Object[]{highestSeasonNumber + 1}, LocaleContextHolder.getLocale());
		Season season = new Season(title, "", highestSeasonNumber + 1, show);
		show.getSeasons().add(season);

		return season;
	}

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
						.filter(episode -> episode.getLengthInMinutes() != null)
						.mapToInt(Episode::getLengthInMinutes))
				.sum();
	}

	@Transactional
	public void addApiIdentifier(UUID showId, APIIdentifier apiIdentifier)
	{
		final Optional<Show> showOptional = getById(showId);
		if(showOptional.isEmpty())
		{
			throw new NotFoundException("Show not found");
		}

		final Show show = showOptional.get();

		if(show.getApiIdentifierByType(apiIdentifier.getType()).isPresent())
		{
			throw new IllegalArgumentException(MessageFormat.format("Show {0} already has an api identifier of type {1}", show.getName(), apiIdentifier.getType().name()));
		}

		apiIdentifier.setShow(show);
		show.getApiIdentifiers().add(apiIdentifier);
	}
}
