package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.season.reposiroty.SeasonRepository;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DatabaseExporter
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseExporter.class);

	private final ShowRepository showRepository;
	private final SeasonRepository seasonRepository;
	private final EpisodeRepository episodeRepository;
	private final UserRepository userRepository;

	@Autowired
	public DatabaseExporter(ShowRepository showRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository, UserRepository userRepository)
	{
		this.showRepository = showRepository;
		this.seasonRepository = seasonRepository;
		this.episodeRepository = episodeRepository;
		this.userRepository = userRepository;
	}

	public void export() throws IOException
	{
		LOGGER.info("Exporting database...");
		final Database databaseToExport = new Database(showRepository.findAll(), seasonRepository.findAll(), episodeRepository.findAll(), userRepository.findAll());
		LOGGER.info("Export done");
	}
}
