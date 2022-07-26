package ca.homedepot.preference.exception;

import lombok.Getter;


/**
 * The type Homedepot ca custom exception.
 */
@Getter
public class PreferenceCenterCustomException extends RuntimeException
{
	/**
	 * The Error response.
	 */
	private ErrorResponse errorResponse;
	/**
	 * The Loggable.
	 */
	private Boolean loggable = true;

	/**
	 * Instantiates a new Homedepot ca custom exception.
	 *
	 * @param message
	 *           the message
	 * @param errorCodes
	 *           the error codes
	 */
	public PreferenceCenterCustomException(final String message, final ErrorCodes errorCodes)
	{
		super(message);
		this.errorResponse = new ErrorResponse(errorCodes, message);
	}

	/**
	 * Instantiates a new Homedepot ca custom exception.
	 *
	 * @param message
	 *           the message
	 */
	public PreferenceCenterCustomException(String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new Homedepot ca custom exception.
	 *
	 * @param exception
	 *           the exception
	 */
	public PreferenceCenterCustomException(Exception exception)
	{
		super(exception);
	}
}
