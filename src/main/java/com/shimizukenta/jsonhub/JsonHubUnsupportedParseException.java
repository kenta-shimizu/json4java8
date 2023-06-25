package com.shimizukenta.jsonhub;

/**
 * JsonHub Unsupported Parse Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonHubUnsupportedParseException extends JsonHubParseException {

	private static final long serialVersionUID = 2351099713814673110L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonHubUnsupportedParseException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonHubUnsupportedParseException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public JsonHubUnsupportedParseException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public JsonHubUnsupportedParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
