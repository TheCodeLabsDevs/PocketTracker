package de.thecodelabs.pockettracker.user.model;

import de.thecodelabs.pockettracker.show.model.Show;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "appuser_hidden_shows")
@IdClass(HiddenShow.HiddenShowId.class)
@Getter
@NoArgsConstructor
public class HiddenShow
{
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class HiddenShowId implements Serializable
	{
		private UUID userId;
		private UUID showId;
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

	public HiddenShow(@NotNull User user, @NotNull Show show)
	{
		this.user = user;
		this.show = show;

		this.userId = user.getId();
		this.showId = show.getId();
	}
}
