package de.thecodelabs.pockettracker.show.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowService;
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

	@GetMapping
	@JsonView(Show.View.Summary.class)
	public List<Show> getAllShows(@RequestParam(name = "name", required = false) String name)
	{
		return showService.getAllShows(name);
	}

	@GetMapping("/{id}")
	@JsonView(Show.View.Summary.class)
	public Optional<Show> getAllShows(@PathVariable Integer id)
	{
		return showService.getShowById(id);
	}
}
