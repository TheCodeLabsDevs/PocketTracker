package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.show.model.Show;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "appuser_added_shows")
@IdClass(AddedShow.AddedShowId.class)
public class AddedShow
{
	public static class AddedShowId implements Serializable
	{
		private Integer userId;
		private Integer showId;

		public AddedShowId()
		{
		}

		public AddedShowId(Integer userId, Integer showId)
		{
			this.userId = userId;
			this.showId = showId;
		}

		public Integer getUserId()
		{
			return userId;
		}

		public Integer getShowId()
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
	protected Integer userId;
	@Id
	@Column(name = "show_id")
	protected Integer showId;

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
