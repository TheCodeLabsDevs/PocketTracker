package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.show.model.Show;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "appuser_added_shows")
@IdClass(AddedShow.AddedShowId.class)
public class AddedShow
{
	public static class AddedShowId implements Serializable
	{
		private UUID userId;
		private UUID showId;

		public AddedShowId()
		{
		}

		public AddedShowId(UUID userId, UUID showId)
		{
			this.userId = userId;
			this.showId = showId;
		}

		public UUID getUserId()
		{
			return userId;
		}

		public UUID getShowId()
		{
			return showId;
		}

		@Override
		public boolean equals(Object o)
		{
			if(this == o) return true;
			if(!(o instanceof AddedShowId that)) return false;
			return Objects.equals(userId, that.userId) && Objects.equals(showId, that.showId);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(userId, showId);
		}
	}

	@Id
	@Column(name = "user_id")
	protected UUID userId;
	@Id
	@Column(name = "show_id")
	protected UUID showId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "show_id", insertable = false, updatable = false)
	private Show show;

	@Column(name = "disliked")
	private Boolean disliked;

	public AddedShow()
	{
	}

	public AddedShow(@NotNull User user, @NotNull Show show, Boolean disliked)
	{
		this.user = user;
		this.show = show;
		this.disliked = disliked;

		this.userId = user.getId();
		this.showId = show.getId();
	}

	public User getUser()
	{
		return user;
	}

	public Show getShow()
	{
		return show;
	}

	public Boolean getDisliked()
	{
		return disliked;
	}

	public void setDisliked(Boolean disliked)
	{
		this.disliked = disliked;
	}
}
