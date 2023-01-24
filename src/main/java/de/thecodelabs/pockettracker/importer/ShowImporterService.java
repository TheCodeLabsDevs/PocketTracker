package de.thecodelabs.pockettracker.importer;

import de.thecodelabs.pockettracker.importer.factory.ImporteNotConfiguredException;
import de.thecodelabs.pockettracker.importer.model.ShowSearchItem;
import de.thecodelabs.pockettracker.show.model.Show;

import java.io.IOException;
import java.util.List;

public interface ShowImporterService
{
	List<ShowSearchItem> searchForShow(String search) throws ImporteNotConfiguredException, ImportProcessException, IOException;

	Show createShow(String identifier) throws ImporteNotConfiguredException, IOException, ImportProcessException;
}
