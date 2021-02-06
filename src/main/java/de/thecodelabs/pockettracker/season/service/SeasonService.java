package de.thecodelabs.pockettracker.season.service;

import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.reposiroty.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeasonService
{
	private final SeasonRepository seasonRepository;

	@Autowired
	public SeasonService(SeasonRepository seasonRepository)
	{
		this.seasonRepository = seasonRepository;
	}

	public Optional<Season> getSeasonById(Integer id)
	{
		return seasonRepository.findById(id);
	}
}
