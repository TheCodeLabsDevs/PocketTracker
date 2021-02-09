package de.thecodelabs.pockettracker.show.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/show")
@Tag(name = "Show")
public class ShowApiController
{
	private final ShowService showService;

	@Autowired
	public ShowApiController(ShowService showService)
	{
		this.showService = showService;
	}

	@Operation(operationId = "getAllShows")
	@GetMapping
	@JsonView(Show.View.Summary.class)
	public List<Show> getAllShows(@RequestParam(name = "name", required = false) String name)
	{
		return showService.getAllShows(name);
	}

	@Operation(operationId = "getShowById")
	@GetMapping("/{id}")
	public Optional<Show> getAllShows(@PathVariable Integer id)
	{
		return showService.getShowById(id);
	}
}
