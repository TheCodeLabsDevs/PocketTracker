package de.thecodelabs.pockettracker.movie;

import de.thecodelabs.pockettracker.configuration.WebConfigurationProperties;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemRepository;
import de.thecodelabs.pockettracker.mediaitem.BaseMediaItemService;
import de.thecodelabs.pockettracker.movie.model.Movie;
import de.thecodelabs.pockettracker.show.model.APIIdentifier;
import de.thecodelabs.pockettracker.user.model.AddedMovie;
import de.thecodelabs.pockettracker.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService extends BaseMediaItemService<Movie>
{
	public MovieService(BaseMediaItemRepository<Movie> repository, WebConfigurationProperties webConfigurationProperties)
	{
		super(repository, webConfigurationProperties);
	}

	public List<Movie> getAllFavoriteByUser(String name, User user)
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
		return sort(movies);
	}

	@Override
	protected void prepareItem(Movie item)
	{
	}

	@Transactional
	public void addApiIdentifier(UUID movieId, APIIdentifier apiIdentifier)
	{
		final Optional<Movie> movieOptional = getById(movieId);
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
