package com.shimizukenta.jsonhub;

/**
 * JsonPath parse Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonPathParseException extends RuntimeException {
	
	private static final long serialVersionUID = 3102503542744302116L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonPathParseException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonPathParseException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public JsonPathParseException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public JsonPathParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enableSuppression
	 * @param writableStackTrace the writableStackTrace
	 */
	public JsonPathParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
