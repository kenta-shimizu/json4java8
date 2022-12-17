package com.shimizukenta.jsonhub;

/**
 * 
 * @author kenta-shimizu
 *
 */
public class JsoncReaderException extends RuntimeException {
	
	private static final long serialVersionUID = 7010332574569159621L;
	
	public JsoncReaderException() {
		super();
	}

	public JsoncReaderException(String message) {
		super(message);
	}

	public JsoncReaderException(Throwable cause) {
		super(cause);
	}

	public JsoncReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsoncReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
