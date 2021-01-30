package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.episode.EpisodeRepository;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.Season;
import de.thecodelabs.pockettracker.season.SeasonRepository;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.DiscriminatorValue;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController
{
	private final UserService userService;
	private final ShowRepository showRepository;
	private final SeasonRepository seasonRepository;
	private final EpisodeRepository episodeRepository;

	@Autowired
	public UserController(UserService userService, ShowRepository showRepository, SeasonRepository seasonRepository, EpisodeRepository episodeRepository)
	{
		this.userService = userService;
		this.showRepository = showRepository;
		this.seasonRepository = seasonRepository;
		this.episodeRepository = episodeRepository;
	}

	@GetMapping("/settings")
	public String settingsView(WebRequest request, Model model)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final User user = userOptional.get();
		model.addAttribute("user", new UserForm(user));
		model.addAttribute("authentications", user.getAuthentications().stream()
				.map(userAuthentication -> userAuthentication.getClass().getAnnotation(DiscriminatorValue.class).value())
				.collect(Collectors.toList()));

		return "users/edit";
	}

	@PostMapping("/settings")
	public String settingsSubmit(WebRequest request, @ModelAttribute("user") UserForm userForm)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		try
		{
			userService.editUser(userOptional.get(), userForm);
		}
		catch(PasswordValidationException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.validate_password", BootstrapColor.DANGER));
			return "redirect:/user/settings";
		}

		WebRequestUtils.putToast(request, new Toast("toast.saved", BootstrapColor.SUCCESS));
		return "redirect:/user/settings";
	}

	@GetMapping("/shows")
	public String getShows(Model model)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final User user = userOptional.get();
		model.addAttribute("currentPage", "Meine Serien");
		model.addAttribute("shows", user.getShows());
		model.addAttribute("userShows", user.getShows());
		model.addAttribute("isUserSpecificView", true);

		return "index";
	}

	@GetMapping("/shows/add/{showId}")
	@Transactional
	public String addShow(WebRequest request, @PathVariable Integer showId)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userOptional.get();
		user.getShows().add(showOptional.get());

		return "redirect:/shows";
	}

	@GetMapping("/shows/remove/{showId}")
	@Transactional
	public String removeShow(WebRequest request, @PathVariable Integer showId)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userOptional.get();
		final Show showToRemove = showOptional.get();
		final boolean userHadShow = user.getShows().remove(showToRemove);
		if(!userHadShow)
		{
			WebRequestUtils.putToast(request, new Toast("Der Nutzer hatte die Serie nie hinzugefügt.", BootstrapColor.WARNING));
			return "redirect:/user/shows";
		}

		final List<Episode> watchedEpisodesByShow = userService.getWatchedEpisodesByShow(user, showToRemove);
		user.getWatchedEpisodes().removeAll(watchedEpisodesByShow);

		return "redirect:/user/shows";
	}

	@GetMapping("/season/{seasonId}")
	@Transactional
	public String setSeasonAsWatched(WebRequest request,
									 @PathVariable Integer seasonId,
									 @RequestParam(name = "markAsWatched") boolean markAsWatched)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final Optional<Season> seasonOptional = seasonRepository.findById(seasonId);
		if(seasonOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Staffel mit der ID \"{0}\"", seasonId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userOptional.get();
		final Season season = seasonOptional.get();
		userService.toggleCompleteSeason(user, season, markAsWatched);

		return "redirect:/season/" + season.getId();
	}

	@GetMapping("/episode/{episodeId}/toggle/{redirectTo}")
	@Transactional
	public String toggleEpisode(WebRequest request, @PathVariable Integer episodeId, @PathVariable String redirectTo)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final Optional<Episode> episodeOptional = episodeRepository.findById(episodeId);
		if(episodeOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Episode mit der ID \"{0}\"", episodeId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userOptional.get();

		final Episode episode = episodeOptional.get();
		if(user.getWatchedEpisodes().contains(episode))
		{
			user.getWatchedEpisodes().remove(episode);
		}
		else
		{
			user.getWatchedEpisodes().add(episode);
		}

		if(redirectTo.equals("episode"))
		{
			return "redirect:/episode/" + episode.getId();
		}

		return "redirect:/season/" + episode.getSeason().getId();
	}

	@GetMapping("/statistics")
	public String statistics(Model model)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final User user = userOptional.get();
		model.addAttribute("currentPage", "Statistiken");
		model.addAttribute("statisticItems", userService.getStatistics(user));

		return "statistics";
	}
}
