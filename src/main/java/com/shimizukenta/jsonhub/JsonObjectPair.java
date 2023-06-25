package com.shimizukenta.jsonhub;

import com.shimizukenta.jsonhub.impl.JsonObjectPairImpl;

/**
 * this interface is pair of Object name ans value.
 * 
 * @see JsonString
 * @author kenta-shimizu
 *
 */
public interface JsonObjectPair {
	
	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value value of Object
	 * @return JsonObjectPair instance
	 */
	public static JsonObjectPair of(JsonString name, JsonHub value) {
		return new JsonObjectPairImpl(name, value);
	}
	
	/**
	 * Returns name of pair.
	 * 
	 * @return name
	 */
	JsonString name();

	/**
	 * Returns value of pair.
	 * 
	 * @return value
	 */
	JsonHub value();

}