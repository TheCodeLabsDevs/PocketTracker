package de.thecodelabs.pockettracker.importer;

import de.thecodelabs.pockettracker.importer.factory.ImporterNotConfiguredException;
import de.thecodelabs.pockettracker.importer.model.MovieSearchItem;
import de.thecodelabs.pockettracker.movie.model.Movie;

import java.io.IOException;
import java.util.List;

public interface MovieImporterService
{
	List<MovieSearchItem> searchForMovie(String idToSearch) throws ImporterNotConfiguredException, ImportProcessException, IOException;

	Movie createMovie(String identifier) throws ImporterNotConfiguredException, IOException, ImportProcessException;

	List<String> getMoviePosterImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporterNotConfiguredException;

	Movie updateMovie(String identifier, Movie existingMovie) throws IOException, ImportProcessException, ImporterNotConfiguredException;
}
