package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.episode.repository.EpisodeRepository;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.season.reposiroty.SeasonRepository;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.WatchedEpisode;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import de.thecodelabs.pockettracker.user.service.UserAuthenticationService;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.thecodelabs.pockettracker.MainController.PARAMETER_NAME_IS_USER_SPECIFIC_VIEW;
import static de.thecodelabs.pockettracker.MainController.PARAMETER_NAME_SEARCH_TEXT;

@Controller
@RequestMapping("/user")
public class UserController
{
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;
	private final UserAuthenticationService authenticationService;

	private final ShowRepository showRepository;
	private final ShowService showService;
	private final SeasonRepository seasonRepository;
	private final EpisodeRepository episodeRepository;

	@Autowired
	public UserController(UserService userService, UserAuthenticationService authenticationService, ShowRepository showRepository, ShowService showService, SeasonRepository seasonRepository, EpisodeRepository episodeRepository)
	{
		this.userService = userService;
		this.authenticationService = authenticationService;
		this.showRepository = showRepository;
		this.showService = showService;
		this.seasonRepository = seasonRepository;
		this.episodeRepository = episodeRepository;
	}

	@GetMapping("/settings")
	public String settingsView(HttpServletRequest request, Model model)
	{
		final Optional<User> userOptional = userService.getCurrentUserOptional();
		if(userOptional.isEmpty())
		{
			try
			{
				request.logout();
			}
			catch(ServletException e)
			{
				logger.error("Cannot logout user", e);
			}
			return "redirect:/";
		}

		final User user = userOptional.get();
		model.addAttribute("user", new UserForm(user));
		model.addAttribute("authentications", user.getAuthentications().stream().sorted(Comparator.comparing(UserAuthentication::getType)).collect(Collectors.toList()));
		model.addAttribute("isGitlabConnected", user.getAuthentications().stream().anyMatch(provider -> provider instanceof GitlabAuthentication));

		return "users/edit";
	}

	@PostMapping("/settings")
	public String settingsSubmit(WebRequest request, @ModelAttribute("user") UserForm userForm)
	{
		try
		{
			userService.editUser(userService.getCurrentUser(), userForm);
		}
		catch(PasswordValidationException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.validatePassword", BootstrapColor.DANGER));
			return "redirect:/user/settings";
		}

		WebRequestUtils.putToast(request, new Toast("toast.saved", BootstrapColor.SUCCESS));
		return "redirect:/user/settings";
	}

	@PostMapping("/settings/oauth/{provider}")
	public String connectOauth(WebRequest request, @PathVariable String provider,
							   @ModelAttribute("oauth") @Validated UserOauthForm oauthForm, BindingResult result)
	{
		if(result.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("authentication.provider.gitlab.missingUsername", BootstrapColor.DANGER));
			return "redirect:/user/settings";
		}

		Optional<User> userOptional = userService.getUser(SecurityContextHolder.getContext().getAuthentication());
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		final User user = userOptional.get();
		userService.addGitlabAuthentication(user, oauthForm.getUsername());

		return "redirect:/oauth2/authorization/" + provider;
	}

	@PostMapping("/settings/provider/{id}/delete")
	public String deleteProvider(@PathVariable Integer id)
	{
		authenticationService.deleteAuthenticationProvider(userService.getCurrentUser(), id);
		return "redirect:/user/settings";
	}

	@GetMapping("/shows")
	public String getShows(Model model)
	{
		final User user = userService.getCurrentUser();

		String searchText = null;
		if(model.containsAttribute(PARAMETER_NAME_SEARCH_TEXT))
		{
			searchText = (String) model.getAttribute(PARAMETER_NAME_SEARCH_TEXT);
		}

		model.addAttribute("shows", showService.getAllFavoriteShowsByUser(searchText, user));

		model.addAttribute("currentPage", "Meine Serien");
		model.addAttribute("userShows", user.getShows());
		model.addAttribute(PARAMETER_NAME_IS_USER_SPECIFIC_VIEW, true);

		return "index";
	}

	@GetMapping("/shows/add/{showId}")
	@Transactional
	public String addShow(WebRequest request, @PathVariable Integer showId)
	{
		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userService.getCurrentUser();
		user.getShows().add(showOptional.get());

		return "redirect:/shows";
	}

	@GetMapping("/shows/remove/{showId}")
	@Transactional
	public String removeShow(WebRequest request, @PathVariable Integer showId)
	{
		final Optional<Show> showOptional = showRepository.findById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userService.getCurrentUser();
		final Show showToRemove = showOptional.get();
		final boolean userHadShow = user.getShows().remove(showToRemove);
		if(!userHadShow)
		{
			WebRequestUtils.putToast(request, new Toast("Der Nutzer hatte die Serie nie hinzugefügt.", BootstrapColor.WARNING));
			return "redirect:/user/shows";
		}

		final List<WatchedEpisode> watchedEpisodesByShow = userService.getWatchedEpisodesByShow(user, showToRemove);
		user.getWatchedEpisodes().removeAll(watchedEpisodesByShow);

		return "redirect:/user/shows";
	}

	@GetMapping("/season/{seasonId}")
	@Transactional
	public String setSeasonAsWatched(WebRequest request,
									 @PathVariable Integer seasonId,
									 @RequestParam(name = "markAsWatched") boolean markAsWatched)
	{
		final Optional<Season> seasonOptional = seasonRepository.findById(seasonId);
		if(seasonOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Staffel mit der ID \"{0}\"", seasonId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userService.getCurrentUser();
		final Season season = seasonOptional.get();
		userService.toggleCompleteSeason(user, season, markAsWatched);

		return "redirect:/season/" + season.getId();
	}

	@GetMapping("/episode/{episodeId}/toggle/{redirectTo}")
	public String toggleEpisode(WebRequest request, @PathVariable Integer episodeId, @PathVariable String redirectTo)
	{
		final Optional<Episode> episodeOptional = episodeRepository.findById(episodeId);
		if(episodeOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Episode mit der ID \"{0}\"", episodeId), BootstrapColor.DANGER));
			return "redirect:/shows";
		}

		final User user = userService.getCurrentUser();

		final Episode episode = episodeOptional.get();
		if(userService.isWatchedEpisode(user, episode))
		{
			userService.removeWatchedEpisode(user, episode);
		}
		else
		{
			userService.addWatchedEpisode(user, episode);
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
		final User user = userService.getCurrentUser();
		model.addAttribute("currentPage", "Statistiken");
		model.addAttribute("statisticItems", userService.getStatistics(user));

		return "statistics";
	}
}
