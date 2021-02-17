package de.thecodelabs.pockettracker.backup;

import de.thecodelabs.pockettracker.backup.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@ConditionalOnProperty("pockettracker.backup.background-enabled")
public class BackupScheduler
{
	private final BackupService pocketTrackerExportService;

	@Autowired
	public BackupScheduler(BackupService pocketTrackerExportService)
	{
		this.pocketTrackerExportService = pocketTrackerExportService;
	}

	@Scheduled(cron = "#{backupConfigurationProperties.backgroundScheduled}")
	public void createBackup() throws IOException
	{
		pocketTrackerExportService.createBackup();
	}
}
