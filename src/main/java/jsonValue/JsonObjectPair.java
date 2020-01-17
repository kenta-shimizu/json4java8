package jsonValue;

public class JsonObjectPair {
	
	private final String name;
	private final JsonValue v;
	
	public JsonObjectPair(CharSequence name, JsonValue v) {
		this.name = name.toString();
		this.v = v;
	}
	
	public String name() {
		return name;
	}
	
	public JsonValue value() {
		return v;
	}
}
