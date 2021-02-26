package de.thecodelabs.pockettracker.episode.service;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.model.EpisodeImageType;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.utils.Helpers;
import de.thecodelabs.utils.io.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EpisodeService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeService.class);

	private final EpisodeRepository episodeRepository;
	private final WebConfigurationProperties webConfigurationProperties;

	@Autowired
	public EpisodeService(EpisodeRepository episodeRepository, WebConfigurationProperties webConfigurationProperties)
	{
		this.episodeRepository = episodeRepository;
		this.webConfigurationProperties = webConfigurationProperties;
	}

	public Optional<Episode> getEpisodeById(Integer id)
	{
		return episodeRepository.findById(id);
	}

	public Optional<Episode> getEpisodeByNumbers(Integer showId, Integer seasonNumber, Integer episodeNumber)
	{
		return episodeRepository.findByNumberAndSeasonNumberAndSeasonShowId(episodeNumber, seasonNumber, showId);
	}

	public Optional<Episode> getPreviousEpisode(Episode episode)
	{
		final Season seasonOfEpisode = episode.getSeason();
		final Optional<Episode> nextEpisodeOfCurrentSeason = seasonOfEpisode.getEpisodes().stream()
				.filter(e -> e.getNumber() < episode.getNumber())
				.max(Comparator.comparing(Episode::getNumber));

		if(nextEpisodeOfCurrentSeason.isPresent())
		{
			return nextEpisodeOfCurrentSeason;
		}

		final Optional<Season> nextSeason = seasonOfEpisode.getShow().getSeasons().stream()
				.filter(s -> s.getNumber() < seasonOfEpisode.getNumber())
				.max(Comparator.comparing(Season::getNumber));

		if(nextSeason.isEmpty())
		{
			return Optional.empty();
		}

		return nextSeason.get().getEpisodes().stream().max(Comparator.comparing(Episode::getNumber));
	}

	public Optional<Episode> getNextEpisode(Episode episode)
	{
		final Season seasonOfEpisode = episode.getSeason();
		final Optional<Episode> nextEpisodeOfCurrentSeason = seasonOfEpisode.getEpisodes().stream()
				.filter(e -> e.getNumber() > episode.getNumber())
				.min(Comparator.comparing(Episode::getNumber));

		if(nextEpisodeOfCurrentSeason.isPresent())
		{
			return nextEpisodeOfCurrentSeason;
		}

		final Optional<Season> nextSeason = seasonOfEpisode.getShow().getSeasons().stream()
				.filter(s -> s.getNumber() > seasonOfEpisode.getNumber())
				.min(Comparator.comparing(Season::getNumber));

		if(nextSeason.isEmpty())
		{
			return Optional.empty();
		}

		return nextSeason.get().getEpisodes().stream().min(Comparator.comparing(Episode::getNumber));
	}

	public void createEpisodes(List<Episode> episodes)
	{
		episodeRepository.saveAll(episodes);
	}

	public void deleteEpisode(Episode episode)
	{
		episodeRepository.delete(episode);
	}

	@Transactional
	public void changeEpisodeImage(EpisodeImageType episodeImageType, Episode episode, MultipartFile file) throws IOException
	{
		deleteEpisodeImage(episodeImageType, episode);

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());

		final String extension = Optional.ofNullable(file.getOriginalFilename()).map(PathUtils::getFileExtension).orElse("jpg");
		final String showName = Helpers.replaceNonAlphaNumericCharacters(episode.getSeason().getShow().getName(), "_");
		final String fileName = MessageFormat.format("{0}+{1}.{2}", showName, Helpers.getShortCode(episode), extension);
		final String escapedFileName = Helpers.replaceNonAlphaNumericCharacters(fileName, "_");

		final Path bannerPath = basePath.resolve(episodeImageType.getPathName()).resolve(escapedFileName);
		if(Files.notExists(bannerPath.getParent()))
		{
			Files.createDirectories(bannerPath.getParent());
		}

		episode.setImagePath(episodeImageType, basePath.relativize(bannerPath).toString());
		file.transferTo(bannerPath);
	}

	@Transactional
	public void deleteEpisodeImage(EpisodeImageType episodeImageType, Episode episode)
	{
		if(episode.getImagePath(episodeImageType) == null)
		{
			return;
		}

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());
		final Path bannerPath = basePath.resolve(episode.getImagePath(episodeImageType));

		try
		{
			Files.deleteIfExists(bannerPath);
			episode.setImagePath(episodeImageType, null);
		}
		catch(IOException e)
		{
			LOGGER.error("Fail to delete poster image", e);
		}
	}
}
