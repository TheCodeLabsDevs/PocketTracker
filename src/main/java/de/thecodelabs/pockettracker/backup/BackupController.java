package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.backup.configuration.BackupConfigurationProperties;
import de.thecodelabs.pockettracker.backup.service.BackupService;
import de.thecodelabs.pockettracker.utils.BootstrapColor;
import de.thecodelabs.pockettracker.utils.WebRequestUtils;
import de.thecodelabs.pockettracker.utils.toast.Toast;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@Controller
@RequestMapping("/administration/backup")
@PreAuthorize("@perm.hasPermission(T(de.thecodelabs.pockettracker.user.model.UserRole).ADMIN)")
@Slf4j
public class BackupController
{
	private final BackupService backupService;
	private final BackupConfigurationProperties backupConfigurationProperties;

	private static class ModelAttributes
	{
		public static final String BACKUPS = "backups";
	}

	private static class ReturnValues
	{
		public static final String ADMIN_BACKUP = "administration/backup";
		public static final String REDIRECT_ADMIN_BACKUP = "redirect:/administration/backup";
		public static final String REDIRECT_ROOT = "redirect:/";
	}

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
			model.addAttribute(ModelAttributes.BACKUPS, backupService.getBackups());
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.backup-list.error", BootstrapColor.DANGER, e.getMessage()));
		}
		return ReturnValues.ADMIN_BACKUP;
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
			log.error("Error exporting backup", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/export")
	public String createBackup(WebRequest request)
	{
		try
		{
			backupService.createBackup();
			WebRequestUtils.putToast(request, new Toast("toast.exported", BootstrapColor.SUCCESS));
		}
		catch(IOException e)
		{
			WebRequestUtils.putToast(request, new Toast("toast.export.error", BootstrapColor.DANGER));
			log.error("Cannot create backup", e);
		}

		return ReturnValues.REDIRECT_ADMIN_BACKUP;
	}

	@PostMapping("/clear")
	public String clearData(WebRequest request, HttpServletRequest httpRequest)
	{
		backupService.clearData();
		WebRequestUtils.putToast(request, new Toast("toast.clear.done", BootstrapColor.DANGER));

		try
		{
			httpRequest.logout();
		}
		catch(ServletException e)
		{
			log.error("Cannot logout user", e);
		}
		return ReturnValues.REDIRECT_ROOT;
	}

	@PostMapping("/restore")
	public String restoreBackup(WebRequest request, @RequestParam("restore") MultipartFile multipartFile)
	{
		if(multipartFile == null || multipartFile.isEmpty())
		{
			WebRequestUtils.putToast(request, new Toast("toast.restore.upload.error", BootstrapColor.DANGER));
			return ReturnValues.REDIRECT_ADMIN_BACKUP;
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
			log.error("Cannot restore backup", e);
		}

		return ReturnValues.REDIRECT_ADMIN_BACKUP;
	}
}
