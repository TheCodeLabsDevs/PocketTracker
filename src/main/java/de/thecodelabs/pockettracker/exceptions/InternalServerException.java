package de.thecodelabs.pockettracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author tobias ullerich
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException
{
	public InternalServerException()
	{
	}

	public InternalServerException(String message)
	{
		super(message);
	}

	public InternalServerException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
