package ca.homedepot.preference.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * The enum Error codes.
 */
@Getter
@AllArgsConstructor
public enum ErrorCodes
{

	/**
	 * The Generic error.
	 */
	GENERIC_ERROR(50000, "Request Failed", HttpStatus.INTERNAL_SERVER_ERROR),
	/**
	 * The Generic sql error.
	 */
	GENERIC_SQL_ERROR(50001, "Generic SQL Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR),
	/**
	 * The Invalid input data format.
	 */
	INVALID_INPUT_DATA_FORMAT(40002, "Missing/Invalid Argument(s)", HttpStatus.BAD_REQUEST),
	/**
	 * The Record not found.
	 */
	RECORD_NOT_FOUND(40004, "Request Data not found in database", HttpStatus.NOT_FOUND);

	private final int code;

	private final String message;

	private HttpStatus httpStatus;
}
