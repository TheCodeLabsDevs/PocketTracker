package de.thecodelabs.pockettracker.importer;

import de.thecodelabs.pockettracker.importer.factory.ImporterNotConfiguredException;
import de.thecodelabs.pockettracker.importer.model.ShowSearchItem;
import de.thecodelabs.pockettracker.season.model.Season;
import de.thecodelabs.pockettracker.show.controller.SeasonInfo;
import de.thecodelabs.pockettracker.show.model.Show;

import java.io.IOException;
import java.util.List;

public interface ShowImporterService
{
	List<ShowSearchItem> searchForShow(String search) throws ImporterNotConfiguredException, ImportProcessException, IOException;

	Show createShow(String identifier) throws ImporterNotConfiguredException, IOException, ImportProcessException;

	List<String> getShowPosterImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporterNotConfiguredException;

	List<String> getShowBannerImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporterNotConfiguredException;

	List<SeasonInfo> getAllAvailableSeasonInfo(Integer identifier) throws ImporterNotConfiguredException, IOException, ImportProcessException;

	Season createSeasonWithEpisodes(Integer identifier, int seasonId) throws IOException, ImportProcessException, ImporterNotConfiguredException;
}
