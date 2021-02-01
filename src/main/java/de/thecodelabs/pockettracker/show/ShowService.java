package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.episode.Episode;
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
import java.util.List;
import java.util.Optional;

@Service
public class ShowService
{
	private static final Logger logger = LoggerFactory.getLogger(ShowService.class);

	private final ShowRepository repository;
	private final WebConfigurationProperties webConfigurationProperties;

	@Autowired
	public ShowService(ShowRepository repository, WebConfigurationProperties webConfigurationProperties)
	{
		this.repository = repository;
		this.webConfigurationProperties = webConfigurationProperties;
	}

	public List<Show> getAllShows(String name)
	{
		if(name == null || name.isEmpty())
		{
			return repository.findAllByOrderByNameAsc();
		}
		else
		{
			return repository.findAllByNameContainingIgnoreCaseOrderByNameAsc(name);
		}
	}

	public Optional<Show> getShowById(Integer id)
	{
		return repository.findById(id);
	}


	public Show createShow(Show show)
	{
		return repository.save(show);
	}

	@Transactional
	public void changeShowImage(ShowImageType showImageType, Show show, MultipartFile file) throws IOException
	{
		deleteShowImage(showImageType, show);

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePath());

		StringBuilder bannerFilenameBuilder = new StringBuilder(Optional.ofNullable(file.getOriginalFilename()).orElse(show.getName()));
		Path bannerPath;

		while(Files.exists(bannerPath = basePath.resolve("banner").resolve(bannerFilenameBuilder.toString())))
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
}
