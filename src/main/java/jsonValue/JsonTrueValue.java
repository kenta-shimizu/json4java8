package jsonValue;

import java.util.Optional;

public class JsonTrueValue extends JsonValue {
	
	protected JsonTrueValue() {
		super();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.TRUE;
	}
	
	@Override
	public Optional<Boolean> optionalBoolean() {
		return Optional.of(Boolean.TRUE);
	}
	
	@Override
	public String toJson() {
		return "true";
	}
	
	@Override
	public String toString() {
		return Boolean.TRUE.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o instanceof JsonTrueValue);
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}
	
}
