package de.thecodelabs.pockettracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author tobias ullerich
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException
{
	public NotFoundException()
	{
	}

	public NotFoundException(String message)
	{
		super(message);
	}
}
