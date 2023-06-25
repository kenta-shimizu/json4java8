package com.shimizukenta.jsonhub;

/**
 * JsonHub Unsupported Operation Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonHubUnsupportedOperationException extends UnsupportedOperationException {
	
	private static final long serialVersionUID = 4075658063031302010L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonHubUnsupportedOperationException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonHubUnsupportedOperationException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public JsonHubUnsupportedOperationException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public JsonHubUnsupportedOperationException(String message, Throwable cause) {
		super(message, cause);
	}

}
