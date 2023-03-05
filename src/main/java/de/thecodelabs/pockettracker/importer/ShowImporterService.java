package de.thecodelabs.pockettracker.importer;

import de.thecodelabs.pockettracker.importer.factory.ImporteNotConfiguredException;
import de.thecodelabs.pockettracker.importer.model.ShowSearchItem;
import de.thecodelabs.pockettracker.show.controller.SeasonInfo;
import de.thecodelabs.pockettracker.show.model.Show;

import java.io.IOException;
import java.util.List;

public interface ShowImporterService
{
	List<ShowSearchItem> searchForShow(String search) throws ImporteNotConfiguredException, ImportProcessException, IOException;

	Show createShow(String identifier) throws ImporteNotConfiguredException, IOException, ImportProcessException;

	List<String> getShowPosterImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporteNotConfiguredException;

	List<String> getShowBannerImageUrls(Integer identifier) throws ImportProcessException, IOException, ImporteNotConfiguredException;

	List<SeasonInfo> getAllAvailableSeasonInfo(Integer identifier) throws ImporteNotConfiguredException, IOException, ImportProcessException;
}
