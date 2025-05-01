package de.thecodelabs.pockettracker.movie;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.movie.model.MovieImageType;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.utils.Helpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService
{
	private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

	private final MovieRepository repository;
	private final WebConfigurationProperties webConfigurationProperties;

	@Autowired
	public MovieService(MovieRepository repository, WebConfigurationProperties webConfigurationProperties)
	{
		this.repository = repository;
		this.webConfigurationProperties = webConfigurationProperties;
	}

	public List<Movie> getAllMovies(String name)
	{
		final List<Movie> movies;
		if(name == null || name.isEmpty())
		{
			movies = repository.findAllByOrderByNameAsc();
		}
		else
		{
			movies = repository.findAllByNameContainingIgnoreCaseOrderByNameAsc(name);
		}

		return movies.stream().sorted(Comparator.comparing(m -> m.getName().toLowerCase())).toList();
	}

	public List<Movie> getAllFavoriteMoviesByUser(String name, User user)
	{
		final List<Movie> movies;
		if(name == null || name.isEmpty())
		{
			movies = user.getMovies()
					.stream()
					.map(AddedMovie::getMovie)
					.toList();
		}
		else
		{
			movies = user.getMovies()
					.stream()
					.map(AddedMovie::getMovie)
					.filter(movie -> movie.getName().toLowerCase().contains(name.toLowerCase()))
					.toList();
		}
		return movies.stream().sorted(Comparator.comparing(m -> m.getName().toLowerCase())).toList();
	}


	public Optional<Movie> getMovieById(UUID id)
	{
		return repository.findById(id);
	}

	public Movie createMovie(Movie movie)
	{
		return repository.save(movie);
	}

	public void createMovie(List<Movie> movies)
	{
		repository.saveAll(movies);
	}

	@Transactional
	public void changeMovieImage(MovieImageType movieImageType, Movie movie, String fileName, InputStream dataStream) throws IOException
	{
		deleteMovieImage(movieImageType, movie);

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());

		final String escapedFileName = Helpers.replaceNonAlphaNumericCharacters(fileName, "_");
		StringBuilder filenameBuilder = new StringBuilder(escapedFileName);
		Path destinationPath;

		while(Files.exists(destinationPath = basePath.resolve(movieImageType.getPathName()).resolve(filenameBuilder.toString())))
		{
			filenameBuilder.insert(0, "_");
		}

		if(Files.notExists(destinationPath.getParent()))
		{
			Files.createDirectories(destinationPath.getParent());
		}

		movie.setImagePath(movieImageType, basePath.relativize(destinationPath).toString());
		FileCopyUtils.copy(dataStream, Files.newOutputStream(destinationPath));
	}

	@Transactional
	public void deleteMovieImage(MovieImageType movieImageType, Movie movie)
	{
		if(movie.getImagePath(movieImageType) == null)
		{
			return;
		}

		final Path basePath = Paths.get(webConfigurationProperties.getImageResourcePathForOS());
		final Path bannerPath = basePath.resolve(movie.getImagePath(movieImageType));

		try
		{
			Files.deleteIfExists(bannerPath);
			movie.setImagePath(movieImageType, null);
		}
		catch(IOException e)
		{
			logger.error("Fail to delete banner image", e);
		}
	}

	@Transactional
	public void deleteMovie(Movie movie)
	{
		repository.delete(movie);
	}

	@Transactional
	public void addApiIdentifierToMovie(UUID movieId, APIIdentifier apiIdentifier)
	{
		final Optional<Movie> movieOptional = getMovieById(movieId);
		if(movieOptional.isEmpty())
		{
			throw new NotFoundException("Movie not found");
		}

		final Movie movie = movieOptional.get();

		if(movie.getApiIdentifierByType(apiIdentifier.getType()).isPresent())
		{
			throw new IllegalArgumentException(MessageFormat.format("Movie {0} already has an api identifier of type {1}", movie.getName(), apiIdentifier.getType().name()));
		}

		apiIdentifier.setMovie(movie);
		movie.getApiIdentifiers().add(apiIdentifier);
	}
}
