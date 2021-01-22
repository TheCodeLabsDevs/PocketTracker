package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.episode.Episode;
import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.show.Show;
import de.thecodelabs.pockettracker.show.ShowRepository;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.User;
import de.thecodelabs.pockettracker.user.UserService;
import de.thecodelabs.pockettracker.utils.Toast;
import de.thecodelabs.pockettracker.utils.ToastColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController
{
	private final UserService userService;
	private final ShowRepository showRepository;

	@Autowired
	public UserController(UserService userService, ShowRepository showRepository)
	{
		this.userService = userService;
		this.showRepository = showRepository;
	}

	@GetMapping
	public String view(WebRequest request, Model model)
	{
		final Optional<User> userOptional = userService.getCurrentUser();
		if(userOptional.isEmpty())
		{
			throw new NotFoundException("User not found");
		}

		model.addAttribute("user", new UserForm(userOptional.get()));
		model.addAttribute("toast", WebRequestUtils.popToast(request));

		return "users/edit";
	}

	@PostMapping
	public String edit(WebRequest request, @ModelAttribute("user") UserForm userForm)
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
			WebRequestUtils.putToast(request, new Toast("Passwort Validierung fehlgeschlagen", ToastColor.DANGER));
			return "redirect:/user";
		}

		WebRequestUtils.putToast(request, new Toast("Änderungen gespeichert", ToastColor.SUCCESS));
		return "redirect:/user";
	}

	@GetMapping("shows")
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

	@GetMapping("shows/add/{showId}")
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
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), ToastColor.DANGER));
			return "redirect:/allShows";
		}

		final User user = userOptional.get();
		user.getShows().add(showOptional.get());

		return "redirect:/allShows";
	}

	@GetMapping("shows/remove/{showId}")
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
			WebRequestUtils.putToast(request, new Toast(MessageFormat.format("Es existiert keine Serie mit der ID \"{0}\"", showId), ToastColor.DANGER));
			return "redirect:/allShows";
		}

		final User user = userOptional.get();
		final Show showToRemove = showOptional.get();
		final boolean userHadShow = user.getShows().remove(showToRemove);
		if(!userHadShow)
		{
			WebRequestUtils.putToast(request, new Toast("Der Nutzer hatte die Serie nie hinzugefügt.", ToastColor.WARNING));
			return "redirect:/user/shows";
		}

		final List<Episode> watchedEpisodesByShow = userService.getWatchedEpisodesByShow(user, showToRemove);
		user.getWatchedEpisodes().removeAll(watchedEpisodesByShow);

		return "redirect:/user/shows";
	}
}
