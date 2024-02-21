package de.thecodelabs.pockettracker.season.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.service.SeasonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/season")
@Tag(name = "Season")
public class SeasonApiController
{
	private final SeasonService seasonService;

	@Autowired
	public SeasonApiController(SeasonService seasonService)
	{
		this.seasonService = seasonService;
	}

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
