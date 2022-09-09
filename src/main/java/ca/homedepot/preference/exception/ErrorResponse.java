package ca.homedepot.preference.exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


/**
 * The type Error response.
 */
@Getter
@Setter
public class ErrorResponse implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String message;

	private int errorCode;

	@JsonIgnore
	private HttpStatus httpStatus;

	/**
	 * Instantiates a new Error response.
	 *
	 * @param errorCodes
	 *           the error codes
	 */
	public ErrorResponse(final ErrorCodes errorCodes)
	{
		super();
		this.message = errorCodes.getMessage();
		this.errorCode = errorCodes.getCode();
		this.httpStatus = errorCodes.getHttpStatus();
	}

	/**
	 * Instantiates a new Error response.
	 *
	 * @param errorCodes
	 *           the error codes
	 * @param customErrorMessage
	 *           the custom error message
	 */
	public ErrorResponse(final ErrorCodes errorCodes, final String customErrorMessage)
	{
		super();
		this.message = errorCodes.getMessage() + " ~ " + customErrorMessage;
		this.errorCode = errorCodes.getCode();
		this.httpStatus = errorCodes.getHttpStatus();
	}
}
