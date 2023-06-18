package com.shimizukenta.jsonhub;

/**
 * JSON structural characters.
 * 
 * @author kenta-shimizu
 *
 */
public enum JsonStructuralChar {
	
	/**
	 * ESCAPE (BACK-SLASH)
	 */
	ESCAPE("\\"),
	
	/**
	 * DOUBLE-QUOTATION "
	 */
	QUOT("\""),
	
	/**
	 * OBJECT=BEGIN {
	 */
	OBJECT_BIGIN("{"),
	
	/**
	 * OBJECT-END }
	 */
	OBJECT_END("}"),
	
	/**
	 * ARRAY-BEGIN [
	 */
	ARRAY_BIGIN("["),
	
	/**
	 * ARRAY-END ]
	 */
	ARRAY_END("]"),
	
	/**
	 * SEPARATOR_VALUE ,
	 */
	SEPARATOR_VALUE(","),
	
	/**
	 * SEPARATOR_NAME :
	 */
	SEPARATOR_NAME(":"),
	;
	
	private final String str;
	private final char chr;
	
	private JsonStructuralChar(String s) {
		this.str = s;
		this.chr = s.charAt(0);
	}
	
	/**
	 * Returns string value.
	 * 
	 * @return string value
	 */
	public String str() {
		return str;
	}
	
	/**
	 * Returns char value.
	 * 
	 * @return char value
	 */
	public char chr() {
		return chr;
	}
	
	/**
	 * Returns true if match CharSequence.
	 * 
	 * @param cs the CharSequence
	 * @return ttue if match CharSequence
	 */
	public boolean match(CharSequence cs) {
		return this.str.contentEquals(cs);
	}
	
	/**
	 * Returns true if is equals c.
	 * 
	 * @param c the character
	 * @return true if is equals c
	 */
	public boolean match(char c) {
		return chr == c;
	}
	
}
