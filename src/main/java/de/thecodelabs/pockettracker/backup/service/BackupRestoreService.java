package de.thecodelabs.pockettracker.backup.service;

import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.show.model.ShowImageType;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupRestoreService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupRestoreService.class);

	private final UserService userService;
	private final ShowService showService;

	@Autowired
	public BackupRestoreService(UserService userService, ShowService showService)
	{
		this.userService = userService;
		this.showService = showService;
	}

	public void clearDatabase()
	{
		final List<User> users = userService.getUsers();
		LOGGER.info("Delete {} users", users.size());

		for(User user : users)
		{
			userService.deleteUser(user);
		}

		final List<Show> shows = showService.getAllShows(null);
		LOGGER.info("Delete {} shows", shows.size());
		for(Show show : shows)
		{
			for(ShowImageType type : ShowImageType.values())
			{
				showService.deleteShowImage(type, show);
			}
			showService.deleteShow(show);
		}
	}
}
