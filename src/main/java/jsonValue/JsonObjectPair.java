package jsonValue;

import java.util.Objects;

public class JsonObjectPair {
	
	private final JsonString name;
	private final JsonValue v;
	
	protected JsonObjectPair(JsonString name, JsonValue v) {
		this.name = Objects.requireNonNull(name, "JsonObjectPair nonNull \"name\"");
		this.v = Objects.requireNonNull(v, "JsonObjectPair nonNull \"value\"");
	}
	
	public JsonString name() {
		return name;
	}
	
	public JsonValue value() {
		return v;
	}
}
