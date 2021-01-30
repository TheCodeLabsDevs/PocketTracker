package de.thecodelabs.pockettracker.show;

import de.thecodelabs.pockettracker.episode.Episode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService
{
	private final ShowRepository repository;

	@Autowired
	public ShowService(ShowRepository repository)
	{
		this.repository = repository;
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
