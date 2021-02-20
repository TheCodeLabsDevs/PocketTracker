package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.backup.configuration.BackupConfigurationProperties;
import de.thecodelabs.pockettracker.backup.service.BackupService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@Controller
@RequestMapping("/administration/backup")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
public class BackupController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BackupController.class);

	private final BackupService backupService;
	private final BackupConfigurationProperties backupConfigurationProperties;

	@Autowired
	public BackupController(BackupService backupService, BackupConfigurationProperties backupConfigurationProperties)
	{
		this.backupService = backupService;
		this.backupConfigurationProperties = backupConfigurationProperties;
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

	@GetMapping("/download/{path}")
	public ResponseEntity<byte[]> downloadBackup(@PathVariable String path)
	{
		try
		{
			final String decode = URLDecoder.decode(path, Charset.defaultCharset());
			final byte[] backupBundled = backupService.getBackupBundled(decode);

			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Content-Disposition", "attachment; filename=\"" + path + ".zip\"");

			return ResponseEntity.status(HttpStatus.OK).headers(headers).body(backupBundled);
		}
		catch(IOException e)
		{
			LOGGER.error("Error exporting backup", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
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

	@PostMapping("/clear")
	public String clearData(WebRequest request, HttpServletRequest httpRequest)
	{
		try
		{
			backupService.clearData();
			WebRequestUtils.putToast(request, new Toast("toast.clear.done", BootstrapColor.DANGER));
		}
		catch(SQLException e)
		{
			LOGGER.error("Failed to delete all data", e);
			WebRequestUtils.putToast(request, new Toast("toast.clear.error", BootstrapColor.DANGER));
		}

		try
		{
			httpRequest.logout();
		}
		catch(ServletException e)
		{
			LOGGER.error("Cannot logout user", e);
		}
		return "redirect:/";
	}

	@PostMapping("/restore")
	public String restoreBackup(WebRequest request, @RequestParam("restore") MultipartFile multipartFile)
	{
		if(multipartFile == null || multipartFile.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast("toast.restore.upload.error", BootstrapColor.DANGER));
			return "redirect:/administration/backup";
		}
		try
		{
			final Path restoreZipPath = Paths.get(backupConfigurationProperties.getLocation(), "backup.zip");
			multipartFile.transferTo(restoreZipPath);
			backupService.restoreBackup(restoreZipPath);
			WebRequestUtils.putToast(request, new Toast("toast.restore.done", BootstrapColor.SUCCESS));
		}
		catch(IOException | SQLException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.restore.error", BootstrapColor.DANGER));
			e.printStackTrace();
		}
		return "redirect:/administration/backup";
	}
}
