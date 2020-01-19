package jsonValue;

import java.util.Optional;

public class JsonFalseValue extends JsonValue {

	protected JsonFalseValue() {
		super();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.FALSE;
	}
	
	@Override
	public Optional<Boolean> optionalBoolean() {
		return Optional.of(Boolean.FALSE);
	}
	
	@Override
	public String toJson() {
		return "false";
	}
	
	@Override
	public String toString() {
		return Boolean.FALSE.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o instanceof JsonFalseValue);
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}
	
}
