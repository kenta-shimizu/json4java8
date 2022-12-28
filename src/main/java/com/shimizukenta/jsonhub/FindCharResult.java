package com.shimizukenta.jsonhub;

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
	 * Find position.
	 * 
	 * <p>
	 * Find position.<br />
	 * -1 if not found.<br />
	 * </p>
	 */
	public final int pos;
	
	public FindCharResult(char c, int pos) {
		this.c = c;
		this.pos = pos;
	}
}
