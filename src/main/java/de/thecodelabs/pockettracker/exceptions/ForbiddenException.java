package de.thecodelabs.pockettracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author tobias ullerich
 */
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException
{
	public ForbiddenException()
	{
	}

	public ForbiddenException(String message)
	{
		super(message);
	}
}
