package jsonValue;

public class JsonValueParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -715521949711541261L;
	
	public JsonValueParseException() {
		super();
	}

	public JsonValueParseException(String message) {
		super(message);
	}

	public JsonValueParseException(Throwable cause) {
		super(cause);
	}

	public JsonValueParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
