package com.shimizukenta.jsonhub;

/**
 * JsonHub Parse Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonHubParseException extends RuntimeException {

	private static final long serialVersionUID = -715521949711541261L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonHubParseException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonHubParseException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public JsonHubParseException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public JsonHubParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
