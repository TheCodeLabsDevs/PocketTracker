package de.thecodelabs.pockettracker.importer;

import de.thecodelabs.pockettracker.importer.factory.ImporterNotConfiguredException;
import de.thecodelabs.pockettracker.importer.model.MovieSearchItem;
import de.thecodelabs.pockettracker.importer.model.ShowSearchItem;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.controller.EpisodeInfo;
import de.thecodelabs.pockettracker.show.controller.SeasonInfo;
import de.thecodelabs.pockettracker.show.model.Show;

import java.io.IOException;
import java.util.List;

public interface MovieImporterService
{
	List<MovieSearchItem> searchForMovie(String idToSearch) throws ImporterNotConfiguredException, ImportProcessException, IOException;

	Movie createMovie(String identifier) throws ImporterNotConfiguredException, IOException, ImportProcessException;

	List<String> getShowPosterImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporterNotConfiguredException;
}
