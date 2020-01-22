package jsonHub;

import java.util.Objects;

public class JsonString {
	
	private String escaped;
	private String unescaped;
	
	private JsonString() {
		escaped = null;
		unescaped = null;
	}
	
	public static JsonString escaped(CharSequence escaped) {
		JsonString inst = new JsonString();
		inst.escaped = Objects.requireNonNull(escaped, "JsonString nonNull \"escaped\"").toString();
		return inst;
	}
	
	public static JsonString unescaped(CharSequence unescaped) {
		JsonString inst = new JsonString();
		inst.unescaped = Objects.requireNonNull(unescaped, "JsonString nonNull \"unescaped\"").toString();
		return inst;
	}
	
	public String escaped() {
		
		synchronized ( this ) {
			
			if ( escaped == null ) {
				escaped = JsonStringCoder.getInstance().escape(unescaped);
			}
			
			return escaped;
		}
	}
	
	public String unescaped() {
		
		synchronized ( this ) {
			
			if ( unescaped == null ) {
				unescaped = JsonStringCoder.getInstance().unescape(escaped);
			}
			
			return unescaped;
		}
	}
	
	/**
	 *  alias of unescaped()
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
