package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.backup.service.BackupService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

@Controller
@RequestMapping("/administration/backup")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class BackupController
{
	private final BackupService backupService;

	@Autowired
	public BackupController(BackupService backupService)
	{
		this.backupService = backupService;
	}

	@GetMapping
	public String backup(WebRequest request, Model model)
	{
		try
		{
			model.addAttribute("backups", backupService.getBackups());
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.backup-list.error", BootstrapColor.DANGER, e.getMessage()));
		}
		return "administration/backup";
	}

	@GetMapping("/export")
	public String exportDatabase(WebRequest request)
	{
		try
		{
			backupService.createBackup();
			WebRequestUtils.putToast(request, new Toast("toast.exported", BootstrapColor.SUCCESS));
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.export.error", BootstrapColor.DANGER));
			e.printStackTrace();
		}

		return "redirect:/administration/backup";
	}
}
