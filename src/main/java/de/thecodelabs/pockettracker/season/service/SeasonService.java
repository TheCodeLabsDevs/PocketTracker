package de.thecodelabs.pockettracker.season.service;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.reposiroty.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SeasonService
{
	private final SeasonRepository seasonRepository;
	private final MessageSource messageSource;

	@Autowired
	public SeasonService(SeasonRepository seasonRepository, MessageSource messageSource)
	{
		this.seasonRepository = seasonRepository;
		this.messageSource = messageSource;
	}

	public Optional<Season> getSeasonById(Integer id)
	{
		return seasonRepository.findById(id);
	}

	@Transactional
	public Episode addEpisodeToSeason(Integer seasonId)
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
