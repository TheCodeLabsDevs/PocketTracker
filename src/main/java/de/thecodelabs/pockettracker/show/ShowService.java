package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowImageType;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
public class ShowService
{
	private static final Logger logger = LoggerFactory.getLogger(ShowService.class);

	private final ShowRepository repository;
	private final MessageSource messageSource;
	private final WebConfigurationProperties webConfigurationProperties;

	@Autowired
	public ShowService(ShowRepository repository, MessageSource messageSource, WebConfigurationProperties webConfigurationProperties)
	{
		this.repository = repository;
		this.messageSource = messageSource;
		this.webConfigurationProperties = webConfigurationProperties;
	}

	public List<Show> getAllShows(String name)
	{
		final List<Show> shows;
		if(name == null || name.isEmpty())
		{
			shows = repository.findAllByOrderByNameAsc();
		}
		else
		{
			shows = repository.findAllByNameContainingIgnoreCaseOrderByNameAsc(name);
		}
		shows.forEach(this::prepareShow);
		return shows;
	}

	public Optional<Show> getShowById(Integer id)
	{
		final Optional<Show> showOptional = repository.findById(id);
		showOptional.ifPresent(this::prepareShow);
		return showOptional;
	}

	private void prepareShow(Show show) {
		show.getSeasons().sort(Comparator.comparingInt(Season::getNumber));
	}

	public Show createShow(Show show)
	{
		return repository.save(show);
	}

	@Transactional
	public Season addSeasonToShow(Integer showId)
	{
		final Optional<Show> showOptional = getShowById(showId);
		if (showOptional.isEmpty()) {
			throw new NotFoundException("Show not found");
		}

		final Show show = showOptional.get();
		final int highestSeasonNumber = show.getSeasons().stream().mapToInt(Season::getNumber).max().orElse(0);

		final String title = messageSource.getMessage("season.defaultName", new Object[]{highestSeasonNumber + 1}, LocaleContextHolder.getLocale());
		Season season = new Season(title, "", highestSeasonNumber + 1, show);
		show.getSeasons().add(season);

		return season;
	}

	@Transactional
	public void changeShowImage(ShowImageType showImageType, Show show, MultipartFile file) throws IOException
	{
		deleteShowImage(showImageType, show);

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePath());

		StringBuilder bannerFilenameBuilder = new StringBuilder(Optional.ofNullable(file.getOriginalFilename()).orElse(show.getName()));
		Path bannerPath;

		while(Files.exists(bannerPath = basePath.resolve(showImageType.getPathName()).resolve(bannerFilenameBuilder.toString())))
		{
			bannerFilenameBuilder.insert(0, "_");
		}

		show.setImagePath(showImageType, basePath.relativize(bannerPath).toString());
		file.transferTo(bannerPath);
	}

	@Transactional
	public void deleteShowImage(ShowImageType showImageType, Show show)
	{
		if(show.getImagePath(showImageType) == null)
		{
			return;
		}

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePath());
		final Path bannerPath = basePath.resolve(show.getImagePath(showImageType));

		try
		{
			Files.deleteIfExists(bannerPath);
			show.setImagePath(showImageType, null);
		}
		catch(IOException e)
		{
			logger.error("Fail to delete banner image", e);
		}
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

	public String getShortCode(Episode episode)
	{
		return MessageFormat.format("[S{0} E{1}]",
				String.format("%02d", episode.getSeason().getNumber()),
				String.format("%02d", episode.getNumber()));
	}

	@Transactional
	public void deleteShow(Show show)
	{
		repository.delete(show);
	}
}
