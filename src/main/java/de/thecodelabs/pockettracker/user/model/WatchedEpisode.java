package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.episode.model.Episode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "appuser_watched_episodes")
@IdClass(WatchedEpisode.WatchedEpisodeId.class)
@Getter
@NoArgsConstructor
public class WatchedEpisode
{
	public static class WatchedEpisodeId implements Serializable
	{
		private UUID userId;
		private UUID episodeId;

		public WatchedEpisodeId()
		{
		}

		public WatchedEpisodeId(UUID userId, UUID episodeId)
		{
			this.userId = userId;
			this.episodeId = episodeId;
		}

		public UUID getUserId()
		{
			return userId;
		}

		public UUID getEpisodeId()
		{
			return episodeId;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(!(o instanceof WatchedEpisodeId that)) return false;
			return Objects.equals(userId, that.userId) && Objects.equals(episodeId, that.episodeId);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(userId, episodeId);
		}
	}

	@Id
	@Column(name = "user_id")
	protected UUID userId;
	@Id
	@Column(name = "episode_id")
	protected UUID episodeId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "episode_id", insertable = false, updatable = false)
	private Episode episode;

	@Setter
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate watchedAt;

	public WatchedEpisode(@NotNull User user, @NotNull Episode episode, LocalDate watchedAt)
	{
		this.user = user;
		this.episode = episode;
		this.watchedAt = watchedAt;

		this.userId = user.getId();
		this.episodeId = episode.getId();
	}
}
