package de.thecodelabs.pockettracker.importer.model;

import de.thecodelabs.pockettracker.administration.apiconfiguration.model.APIType;

public record MovieSearchRequest(APIType apiIdentifierType, String search, String targetUrl)
{
}
