package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.episode.model.Episode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "appuser_watched_episodes")
@IdClass(WatchedEpisode.WatchedEpisodeId.class)
public class WatchedEpisode
{
	public static class WatchedEpisodeId implements Serializable
	{
		private Integer userId;
		private Integer episodeId;

		public WatchedEpisodeId()
		{
		}

		public WatchedEpisodeId(Integer userId, Integer episodeId)
		{
			this.userId = userId;
			this.episodeId = episodeId;
		}

		public Integer getUserId()
		{
			return userId;
		}

		public Integer getEpisodeId()
		{
			return episodeId;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(!(o instanceof WatchedEpisodeId)) return false;
			WatchedEpisodeId that = (WatchedEpisodeId) o;
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
	protected Integer userId;
	@Id
	@Column(name = "episode_id")
	protected Integer episodeId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "episode_id", insertable = false, updatable = false)
	private Episode episode;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate watchedAt;

	public WatchedEpisode()
	{
	}

	public WatchedEpisode(@NotNull User user, @NotNull Episode episode, LocalDate watchedAt)
	{
		this.user = user;
		this.episode = episode;
		this.watchedAt = watchedAt;

		this.userId = user.getId();
		this.episodeId = episode.getId();
	}

	public User getUser()
	{
		return user;
	}

	public Episode getEpisode()
	{
		return episode;
	}

	public LocalDate getWatchedAt()
	{
		return watchedAt;
	}

	public void setWatchedAt(LocalDate watchedAt)
	{
		this.watchedAt = watchedAt;
	}
}
