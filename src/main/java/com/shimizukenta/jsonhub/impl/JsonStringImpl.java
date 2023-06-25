package com.shimizukenta.jsonhub.impl;

import java.io.Serializable;

import com.shimizukenta.jsonhub.JsonString;

/**
 * This class is implements of escape/unescape JSON-String.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonStringImpl implements Serializable, JsonString {
	
	private static final long serialVersionUID = -2222040816285239082L;
	
	/**
	 * Cache escaped.
	 */
	private String escaped;
	
	/**
	 * Cache unescaped.
	 */
	private String unescaped;
	
	private JsonStringImpl() {
		escaped = null;
		unescaped = null;
	}
	
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
	public static JsonStringImpl ofEscaped(CharSequence escaped) {
		JsonStringImpl inst = new JsonStringImpl();
		inst.escaped = escaped.toString();
		return inst;
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
	public static JsonStringImpl ofUnescaped(CharSequence unescaped) {
		JsonStringImpl inst = new JsonStringImpl();
		inst.unescaped = unescaped.toString();
		return inst;
	}
	
	@Override
	public String escaped() {
		
		synchronized ( this ) {
			
			if ( this.escaped == null ) {
				this.escaped = JsonStringCoder.escape(unescaped);
			}
			
			return this.escaped;
		}
	}
	
	@Override
	public String unescaped() {
		
		synchronized ( this ) {
			
			if ( this.unescaped == null ) {
				this.unescaped = JsonStringCoder.unescape(escaped);
			}
			
			return this.unescaped;
		}
	}
	
	@Override
	public int length() {
		return unescaped().length();
	}
	
	@Override
	public boolean isEmpty() {
		return unescaped().isEmpty();
	}
	
	/**
	 * Return unescaped string.
	 * 
	 */
	@Override
	public String toString() {
		return unescaped();
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof JsonStringImpl)) {
			return o.toString().equals(toString());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
