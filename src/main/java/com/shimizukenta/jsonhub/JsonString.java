package com.shimizukenta.jsonhub;

import java.io.Serializable;

/**
 * This class is implements of escape/unescape JSON-String.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonString implements Serializable {
	
	private static final long serialVersionUID = -2222040816285239082L;
	
	/**
	 * Cache escaped.
	 */
	private String escaped;
	
	/**
	 * Cache unescaped.
	 */
	private String unescaped;
	
	private JsonString() {
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
	public static JsonString escaped(CharSequence escaped) {
		JsonString inst = new JsonString();
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
	public static JsonString unescaped(CharSequence unescaped) {
		JsonString inst = new JsonString();
		inst.unescaped = unescaped.toString();
		return inst;
	}
	
	/**
	 * Returns escaped string.
	 * 
	 * @return escaped string
	 */
	public String escaped() {
		
		synchronized ( this ) {
			
			if ( this.escaped == null ) {
				this.escaped = JsonStringCoder.escape(unescaped);
			}
			
			return this.escaped;
		}
	}
	
	/**
	 * Returns unescaped string.
	 * 
	 * @return unescaped string
	 */
	public String unescaped() {
		
		synchronized ( this ) {
			
			if ( this.unescaped == null ) {
				this.unescaped = JsonStringCoder.unescape(escaped);
			}
			
			return this.unescaped;
		}
	}
	
	/**
	 * Returns unescaped#length.
	 * 
	 * @return unescaped#length
	 */
	public int length() {
		return unescaped().length();
	}
	
	/**
	 * Returns unescaped#isEmpty.
	 * 
	 * @return unescaped#isEmpty
	 */
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
		if ((o != null) && (o instanceof JsonString)) {
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
