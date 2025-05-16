package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.movie.model.Movie;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "appuser_added_movies")
@IdClass(AddedMovie.AddedMovieId.class)
@Getter
@NoArgsConstructor
public class AddedMovie
{
	public static class AddedMovieId implements Serializable
	{
		private UUID userId;
		private UUID movieId;

		public AddedMovieId()
		{
		}

		public AddedMovieId(UUID userId, UUID movieId)
		{
			this.userId = userId;
			this.movieId = movieId;
		}

		public UUID getUserId()
		{
			return userId;
		}

		public UUID getMovieId()
		{
			return movieId;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(!(o instanceof AddedMovieId that)) return false;
			return Objects.equals(userId, that.userId) && Objects.equals(movieId, that.movieId);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(userId, movieId);
		}
	}

	@Id
	@Column(name = "user_id")
	protected UUID userId;
	@Id
	@Column(name = "movie_id")
	protected UUID movieId;

	@Column(name = "watched_date")
	@Setter
	protected LocalDate watchedDate;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "movie_id", insertable = false, updatable = false)
	private Movie movie;

	public AddedMovie(@NotNull User user, @NotNull Movie movie, @NotNull LocalDate watchedDate)
	{
		this.user = user;
		this.movie = movie;
		this.watchedDate = watchedDate;

		this.userId = user.getId();
		this.movieId = movie.getId();
	}
}
