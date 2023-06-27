package com.shimizukenta.jsonhub;

/**
 * JsonPath unsupported parse Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonPathUnsupportedParseException extends JsonPathParseException {
	
	private static final long serialVersionUID = 7284639685947525518L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonPathUnsupportedParseException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonPathUnsupportedParseException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public JsonPathUnsupportedParseException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public JsonPathUnsupportedParseException(String message, Throwable cause) {
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
	public JsonPathUnsupportedParseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
