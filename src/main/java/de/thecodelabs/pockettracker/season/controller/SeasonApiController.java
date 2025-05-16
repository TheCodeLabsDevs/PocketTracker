package de.thecodelabs.pockettracker.season.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.season.SeasonService;
import de.thecodelabs.pockettracker.season.model.Season;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/season")
@Tag(name = "Season")
@RequiredArgsConstructor
public class SeasonApiController
{
	private final SeasonService seasonService;

	@Operation(operationId = "getAllSeasons")
	@GetMapping
	@JsonView(Season.View.Summary.class)
	public List<Season> getAllSeasonsForShow(@RequestParam(name = "showId") UUID showId)
	{
		return seasonService.getSeasonsForShow(showId);
	}

	@Operation(operationId = "getSeasonById")
	@GetMapping("/{id}")
	public Optional<Season> getSeasonById(@PathVariable UUID id)
	{
		return seasonService.getSeasonById(id);
	}
}
