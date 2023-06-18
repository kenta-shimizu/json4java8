package com.shimizukenta.jsonhub;

/**
 * JsonHub Number format Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonHubNumberFormatException extends NumberFormatException {

	private static final long serialVersionUID = -3893627025512841689L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonHubNumberFormatException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonHubNumberFormatException(String message) {
		super(message);
	}

}
