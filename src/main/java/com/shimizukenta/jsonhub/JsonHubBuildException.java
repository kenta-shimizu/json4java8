package com.shimizukenta.jsonhub;

/**
 * JsonHub build Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonHubBuildException extends RuntimeException {
	
	private static final long serialVersionUID = -7294512897813785687L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonHubBuildException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonHubBuildException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public JsonHubBuildException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public JsonHubBuildException(String message, Throwable cause) {
		super(message, cause);
	}

}
