package de.thecodelabs.pockettracker.user.controller;

import de.thecodelabs.pockettracker.exceptions.NotFoundException;
import de.thecodelabs.pockettracker.user.PasswordValidationException;
import de.thecodelabs.pockettracker.user.model.User;
import de.thecodelabs.pockettracker.user.model.authentication.GitlabAuthentication;
import de.thecodelabs.pockettracker.user.model.authentication.UserAuthentication;
import de.thecodelabs.pockettracker.user.service.UserAuthenticationService;
import de.thecodelabs.pockettracker.user.service.UserService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user/settings")
public class UserSettingsController
{
	private static final Logger logger = LoggerFactory.getLogger(UserSettingsController.class);

	private final UserService userService;
	private final UserAuthenticationService authenticationService;

	private static class ReturnValues
	{
		public static final String REDIRECT_USER_SETTINGS = "redirect:/user/settings";
	}

	@Autowired
	public UserSettingsController(UserService userService, UserAuthenticationService authenticationService)
	{
		this.userService = userService;
		this.authenticationService = authenticationService;
	}

	@GetMapping
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
		model.addAttribute("authentications", user.getAuthentications().stream().sorted(Comparator.comparing(UserAuthentication::getType)).toList());
		model.addAttribute("isGitlabConnected", user.getAuthentications().stream().anyMatch(GitlabAuthentication.class::isInstance));

		return "users/edit";
	}

	@PostMapping
	public String settingsSubmit(WebRequest request, @ModelAttribute("user") UserForm userForm)
	{
		try
		{
			userService.editUser(userService.getCurrentUser(), userForm);
		}
		catch(PasswordValidationException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.validatePassword", BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_USER_SETTINGS;
		}

		WebRequestUtils.putToast(request, new Toast("toast.saved", BootstrapColor.SUCCESS));
		return ReturnValues.REDIRECT_USER_SETTINGS;
	}

	@PostMapping("/oauth/{provider}")
	public String connectOauth(WebRequest request, @PathVariable String provider,
							   @ModelAttribute("oauth") @Validated UserOauthForm oauthForm, BindingResult result)
	{
		if(result.hasErrors())
		{
			WebRequestUtils.putToast(request, new Toast("authentication.provider.gitlab.missingUsername", BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_USER_SETTINGS;
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

	@PostMapping("/provider/{id}/delete")
	public String deleteProvider(@PathVariable UUID id)
	{
		authenticationService.deleteAuthenticationProvider(userService.getCurrentUser(), id);
		return ReturnValues.REDIRECT_USER_SETTINGS;
	}
}
