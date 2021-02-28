package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/administration/batchEdit")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class BatchEditController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchEditController.class);

	private final ShowService showService;

	@Autowired
	public BatchEditController(ShowService showService)
	{
		this.showService = showService;
	}


	@GetMapping
	public String batchEdit()
	{
		return "administration/batchEdit";
	}


	@Transactional
	@PostMapping("/episodeLength")
	public String episodeLength(WebRequest request, @RequestParam(value = "showId", required = false) Integer showId, @RequestParam(value = "lengthInMinutes", required = false) Integer lengthInMinutes)
	{
		if(showId == null)
		{
			WebRequestUtils.putToast(request, new Toast("toast.batchEdit.showNotExisting.missing", BootstrapColor.DANGER));
			return "redirect:/administration/batchEdit";
		}

		if(lengthInMinutes == null)
		{
			WebRequestUtils.putToast(request, new Toast("toast.batchEdit.lengthInMinutes.missing", BootstrapColor.DANGER));
			return "redirect:/administration/batchEdit";
		}

		final Optional<Show> showOptional = showService.getShowById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast("toast.batchEdit.showNotExisting.error", BootstrapColor.DANGER, String.valueOf(showId)));
		}

		final Show show = showOptional.get();
		final List<Episode> episodes = show.getSeasons().stream()
				.flatMap(season -> season.getEpisodes().stream())
				.collect(Collectors.toList());

		LOGGER.debug(MessageFormat.format("Batch edit: Set episode length to {0} for show with ID {1} ({2})", lengthInMinutes, showId, show.getName()));
		for(Episode episode : episodes)
		{
			episode.setLengthInMinutes(lengthInMinutes);
		}

		WebRequestUtils.putToast(request, new Toast("toast.batchEdit.success", BootstrapColor.SUCCESS, String.valueOf(episodes.size())));
		return "redirect:/administration/batchEdit";
	}

}