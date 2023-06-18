package com.shimizukenta.jsonhub;

/**
 * This is Utility class of find character
 * 
 * @author kenta-shimizu
 *
 */
public final class FindChars {

	private FindChars() {
	}
	
	private static final char NOTFOUND = 0;
	private static final char WS = ' ';
	private static final char ESCAPE = '\\';
	
	private static final FindCharResult notfound = new FindCharResult(NOTFOUND, -1);
	
	/**
	 * Seek Next Character.
	 * 
	 * @param cs reference of charsequence
	 * @param fromIndex fromIndex of cs
	 * @param targets seek targets
	 * @return FindCharResult
	 */
	public static FindCharResult next(CharSequence cs, int fromIndex, char... targets) {
		if ( fromIndex>= 0 ) {
			int len = cs.length();
			for ( int p = fromIndex; p < len; ++p ) {
				char c = cs.charAt(p);
				for ( char t : targets ) {
					if ( c == t ) {
						return new FindCharResult(c, p);
					}
				}
			}
		}
		return notfound;
	}
	
	/**
	 * Seek next character ignore white-space({@code <=0x20}).
	 * 
	 * @param cs reference of character
	 * @param fromIndex from index of cs
	 * @return FindCharResult
	 */
	public static FindCharResult nextIgnoreWhiteSpace(CharSequence cs, int fromIndex) {
		if ( fromIndex >= 0 ) {
			int len = cs.length();
			for ( int p = fromIndex; p < len; ++p ) {
				char c = cs.charAt(p);
				if ( c > WS ) {
					return new FindCharResult(c, p);
				}
			}
		}
		return notfound;
	}
	
	/**
	 * Seek next character ignore ESCAPE (BACK-SLASH).
	 * 
	 * @param cs reference of character
	 * @param fromIndex from index of cs
	 * @param targets seek targets
	 * @return FindCharResult
	 */
	public static FindCharResult nextIgnoreEscape(CharSequence cs, int fromIndex, char... targets) {
		if ( fromIndex >= 0 ) {
			int len = cs.length();
			for ( int p = fromIndex; p < len; ++p ) {
				char c = cs.charAt(p);
				if ( c == ESCAPE ) {
					++p;
				} else {
					for ( char t : targets ) {
						if ( c == t ) {
							return new FindCharResult(c, p);
						}
					}
				}
			}
		}
		return notfound;
	}
	
}
