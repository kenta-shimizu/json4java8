package com.shimizukenta.jsonhub;

import com.shimizukenta.jsonhub.impl.JsonStringImpl;

/**
 * This interface is escape/unescape JSON-String.
 * 
 * @author kenta-shimizu
 *
 */
public interface JsonString {

	/**
	 * Returns escaped string.
	 * 
	 * @return escaped string
	 */
	String escaped();
	
	/**
	 * Returns unescaped string.
	 * 
	 * @return unescaped string
	 */
	public String unescaped();
	
	/**
	 * Returns unescaped#length.
	 * 
	 * @return unescaped#length
	 */
	int length();

	/**
	 * Returns unescaped#isEmpty.
	 * 
	 * @return unescaped#isEmpty
	 */
	boolean isEmpty();
	
	/**
	 * Returns JsonString instance from escaped-String.
	 * 
	 * <p>
	 * Not accept null.<br />
	 * </p>
	 * 
	 * @param escaped String
	 * @return JsonString instance
	 */
	public static JsonString ofEscaped(CharSequence escaped) {
		return JsonStringImpl.ofEscaped(escaped);
	}
	
	/**
	 * Returns JsonString instance from unescaped-String.
	 * 
	 * <p>
	 * Not accept null.<br />
	 * </p>
	 * 
	 * @param unescaped String
	 * @return JsonString instance
	 */
	public static JsonString ofUnescaped(CharSequence unescaped) {
		return JsonStringImpl.ofUnescaped(unescaped);
	}
	
}