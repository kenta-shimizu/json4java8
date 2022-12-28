package com.shimizukenta.jsonhub;

public class JsonPathParseException extends RuntimeException {
	
	private static final long serialVersionUID = 3102503542744302116L;
	
	public JsonPathParseException() {
		super();
	}

	public JsonPathParseException(String message) {
		super(message);
	}

	public JsonPathParseException(Throwable cause) {
		super(cause);
	}

	public JsonPathParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonPathParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
