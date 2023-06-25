package com.shimizukenta.jsonhub.impl;

/**
 * 
 * @author kenta-shimizu
 *
 */
public final class FindCharResult {
	
	/**
	 * Find char.
	 * 
	 * <p>
	 * Find char.<br />
	 * '\u0000' if not found.
	 * </p>
	 */
	public final char c;
	
	/**
	 * Finded position index.
	 * 
	 * <p>
	 * if not found, -1.<br />
	 * </p>
	 */
	public final int pos;
	
	/**
	 * Find character and position result.
	 * 
	 * @param c finded character.
	 * @param pos finded position.
	 */
	public FindCharResult(char c, int pos) {
		this.c = c;
		this.pos = pos;
	}
}
