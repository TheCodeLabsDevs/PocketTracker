package de.thecodelabs.pockettracker.episode.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.service.EpisodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/episode")
@Tag(name = "Episode")
@RequiredArgsConstructor
public class EpisodeApiController
{
	private final EpisodeService episodeService;

	@Operation(operationId = "getEpisodeById")
	@GetMapping("/{id}")
	public Optional<Episode> getEpisodeById(@PathVariable UUID id)
	{
		return episodeService.getEpisodeById(id);
	}

	@Operation(operationId = "getEpisode")
	@GetMapping()
	public Optional<Episode> getEpisodeByNumbers(@RequestParam UUID showId, @RequestParam Integer seasonNumber, @RequestParam Integer episodeNumber)
	{
		return episodeService.getEpisodeByNumbers(showId, seasonNumber, episodeNumber);
	}
}
