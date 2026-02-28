package de.thecodelabs.pockettracker.importer;

public class ImportProcessException extends Exception
{
	public ImportProcessException(String message)
	{
		super(message);
	}

	public ImportProcessException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
