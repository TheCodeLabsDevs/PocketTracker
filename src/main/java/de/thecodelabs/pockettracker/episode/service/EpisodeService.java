package de.thecodelabs.pockettracker.episode.service;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EpisodeService
{
	private final EpisodeRepository episodeRepository;

	@Autowired
	public EpisodeService(EpisodeRepository episodeRepository)
	{
		this.episodeRepository = episodeRepository;
	}

	public Optional<Episode> getEpisodeById(Integer id)
	{
		return episodeRepository.findById(id);
	}

	public Optional<Episode> getEpisodeByNumbers(Integer showId, Integer seasonNumber, Integer episodeNumber)
	{
		return episodeRepository.findByNumberAndSeasonNumberAndSeasonShowId(episodeNumber, seasonNumber, showId);
	}

	public void deleteEpisode(Episode episode)
	{
		episodeRepository.delete(episode);
	}
}
