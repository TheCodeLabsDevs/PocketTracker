package de.thecodelabs.pockettracker.administration.batchedit;

import de.thecodelabs.pockettracker.episode.model.Episode;
import de.thecodelabs.pockettracker.show.ShowService;
import de.thecodelabs.pockettracker.show.model.Show;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/administration/batchEdit")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
@RequiredArgsConstructor
@Slf4j
public class BatchEditController
{
	private final ShowService showService;

	private static class ReturnValues
	{
		public static final String BATCH_EDIT = "administration/batchEdit";
		public static final String REDIRECT_BATCH_EDIT = "redirect:/administration/batchEdit";
	}

	@GetMapping
	public String batchEdit()
	{
		return ReturnValues.BATCH_EDIT;
	}


	@Transactional
	@PostMapping("/episodeLength")
	public String episodeLength(WebRequest request, @RequestParam(value = "showId", required = false) UUID showId, @RequestParam(value = "lengthInMinutes", required = false) Integer lengthInMinutes)
	{
		if(showId == null)
		{
			WebRequestUtils.putToast(request, new Toast("toast.batchEdit.showNotExisting.missing", BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_BATCH_EDIT;
		}

		if(lengthInMinutes == null)
		{
			WebRequestUtils.putToast(request, new Toast("toast.batchEdit.lengthInMinutes.missing", BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_BATCH_EDIT;
		}

		final Optional<Show> showOptional = showService.getById(showId);
		if(showOptional.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast("toast.batchEdit.showNotExisting.error", BootstrapColor.DANGER, String.valueOf(showId)));
			return ReturnValues.REDIRECT_BATCH_EDIT;
		}

		final Show show = showOptional.get();
		final List<Episode> episodes = show.getSeasons().stream()
				.flatMap(season -> season.getEpisodes().stream())
				.toList();

		log.debug("Batch edit: Set episode length to {} for show with ID {} ({})", lengthInMinutes, showId, show.getName());
		for(Episode episode : episodes)
		{
			episode.setLengthInMinutes(lengthInMinutes);
		}

		WebRequestUtils.putToast(request, new Toast("toast.batchEdit.success", BootstrapColor.SUCCESS, String.valueOf(episodes.size())));
		return ReturnValues.REDIRECT_BATCH_EDIT;
	}
}