package de.thecodelabs.pockettracker.episode.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/episode")
@Tag(name = "Episode")
public class EpisodeApiController
{
	private final EpisodeService episodeService;

	@Autowired
	public EpisodeApiController(EpisodeService episodeService)
	{
		this.episodeService = episodeService;
	}

	@Operation(operationId = "getEpisode")
	@GetMapping("/{id}")
	public Optional<Episode> getEpisodeById(@PathVariable Integer id)
	{
		return episodeService.getEpisodeById(id);
	}
}
