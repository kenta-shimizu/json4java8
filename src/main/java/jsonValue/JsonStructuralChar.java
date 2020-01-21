package jsonValue;

import java.util.Objects;

public enum JsonStructuralChar {
	
	BACKSLASH("\\"),
	QUOT("\""),
	OBJECT_LEFT("{"),
	OBJECT_RIGHT("}"),
	ARRAY_LEFT("["),
	ARRAY_RIGHT("]"),
	COMMA(","),
	COLON(":"),
	;
	
	public final String str;
	public final char chr;
	
	private JsonStructuralChar(String s) {
		this.str = s;
		this.chr = s.charAt(0);
	}
	
	public boolean match(CharSequence cs) {
		return Objects.requireNonNull(cs).toString().equals(str);
	}
	
	public boolean match(char c) {
		return chr == c;
	}
}
