package de.thecodelabs.pockettracker.season;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SeasonService
{
	private static final Logger logger = LoggerFactory.getLogger(SeasonService.class);

	private final SeasonRepository seasonRepository;
	private final MessageSource messageSource;

	@Autowired
	public SeasonService(SeasonRepository seasonRepository, MessageSource messageSource)
	{
		this.seasonRepository = seasonRepository;
		this.messageSource = messageSource;
	}

	@PostConstruct
	public void updateMissingAttributes()
	{
		for(Season season : seasonRepository.findAll())
		{
			if(season.getFilledCompletely() == null)
			{
				season.setFilledCompletely(false);
				seasonRepository.save(season);
				logger.debug(MessageFormat.format("Updated season {0}: Set missing attribute \"isFilledCompletely\" to false", season.getId()));
			}
		}
	}

	public Optional<Season> getSeasonById(UUID id)
	{
		return seasonRepository.findById(id);
	}

	public List<Season> getSeasonsForShow(UUID show)
	{
		return seasonRepository.findAllByShowId(show);
	}

	public void createSeasons(List<Season> seasons)
	{
		seasonRepository.saveAll(seasons);
	}

	@Transactional
	public Episode addEpisodeToSeason(UUID seasonId)
	{
		final Optional<Season> seasonOptional = getSeasonById(seasonId);
		if(seasonOptional.isEmpty())
		{
			throw new NotFoundException("Season with id " + seasonId + " not found");
		}
		final Season season = seasonOptional.get();
		final int highestEpisodeNumber = season.getEpisodes().stream().mapToInt(Episode::getNumber).max().orElse(0);

		final String title = messageSource.getMessage("episode.defaultName", new Object[]{highestEpisodeNumber + 1}, LocaleContextHolder.getLocale());
		Episode episode = new Episode(title, highestEpisodeNumber + 1, season);
		season.getEpisodes().add(episode);

		return episode;
	}

	public void deleteSeason(Season season)
	{
		seasonRepository.delete(season);
	}
}
