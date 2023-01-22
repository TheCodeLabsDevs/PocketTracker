package de.thecodelabs.pockettracker.importer;

import de.thecodelabs.pockettracker.importer.factory.ImporteNotConfiguredException;
import de.thecodelabs.pockettracker.show.model.Show;

import java.io.IOException;

public interface ShowImporterService
{
	Show createShow(String identifier) throws ImporteNotConfiguredException, IOException;
}
