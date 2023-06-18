package com.shimizukenta.jsonhub;

/**
 * JsonHub parse Index out of bounds Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonHubIndexOutOfBoundsException extends IndexOutOfBoundsException {
	
	private static final long serialVersionUID = -3112716675173149316L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsonHubIndexOutOfBoundsException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsonHubIndexOutOfBoundsException(String message) {
		super(message);
	}

}
