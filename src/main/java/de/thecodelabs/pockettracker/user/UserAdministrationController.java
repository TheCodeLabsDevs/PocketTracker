package de.thecodelabs.pockettracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/administration")
public class UserAdministrationController
{
	private final UserService userService;

	@Autowired
	public UserAdministrationController(UserService userService)
	{
		this.userService = userService;
	}

	@GetMapping
	public String index(Model model) {
		model.addAttribute("users", userService.getUsers());
		return "users/administration/index";
	}
}
