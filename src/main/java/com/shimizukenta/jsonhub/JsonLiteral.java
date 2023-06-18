package com.shimizukenta.jsonhub;

/**
 * JSON Literals.
 * 
 * @author kenta-shimizu
 *
 */
public enum JsonLiteral {
	
	/**
	 * null
	 */
	NULL("null"),
	
	/**
	 * true
	 */
	TRUE("true"),
	
	/**
	 * false
	 */
	FALSE("false"),
	;
	
	private final String s;
	
	private JsonLiteral(String s) {
		this.s = s;
	}
	
	/**
	 * Matcher.
	 * 
	 * @param cs the character sequence
	 * @return true if matches
	 */
	public boolean match(CharSequence cs) {
		return this.s.contentEquals(cs);
	}
	
	@Override
	public String toString() {
		return this.s;
	}
}
