package com.shimizukenta.jsonhub;

/**
 * JSON value types.
 * 
 * @author kenta-shimizu
 *
 */
public enum JsonHubType {
	
	/**
	 * null
	 */
	NULL,
	
	/**
	 * true
	 */
	TRUE,
	
	/**
	 * false
	 */
	FALSE,
	
	/**
	 * STRING
	 */
	STRING,
	
	/**
	 * NUMBER
	 */
	NUMBER,
	
	/**
	 * ARRAY
	 */
	ARRAY,
	
	/**
	 * OBJECT
	 */
	OBJECT,
	;
	
	private JsonHubType() {
		/* Nothing */
	}
	
}
