package com.shimizukenta.jsonhub;

/**
 * JSON reader Exception.
 * 
 * @author kenta-shimizu
 *
 */
public class JsoncReaderException extends RuntimeException {
	
	private static final long serialVersionUID = 7010332574569159621L;
	
	/**
	 * Constructor.
	 * 
	 */
	public JsoncReaderException() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
	public JsoncReaderException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param cause the cause
	 */
	public JsoncReaderException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public JsoncReaderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * COonstructor.
	 * 
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enableSuppression
	 * @param writableStackTrace the writableStackTrace
	 */
	public JsoncReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
