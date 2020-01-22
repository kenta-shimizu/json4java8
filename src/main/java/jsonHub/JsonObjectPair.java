package jsonHub;

import java.util.Objects;

public class JsonObjectPair {
	
	private final JsonString name;
	private final JsonHub v;
	
	protected JsonObjectPair(JsonString name, JsonHub v) {
		this.name = Objects.requireNonNull(name, "JsonObjectPair nonNull \"name\"");
		this.v = Objects.requireNonNull(v, "JsonObjectPair nonNull \"value\"");
	}
	
	public JsonString name() {
		return name;
	}
	
	public JsonHub value() {
		return v;
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof JsonObjectPair)) {
			return ((JsonObjectPair) o).name().equals(name());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name().hashCode();
	}
	
	@Override
	public String toString() {
		return "(\"" + name() + "\": " + value() + ")";
	}
}