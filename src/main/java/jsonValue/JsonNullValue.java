package jsonValue;

public class JsonNullValue extends JsonValue {

	public JsonNullValue() {
		super();
	}

	@Override
	public JsonValueType type() {
		return JsonValueType.NULL;
	}
	
	@Override
	public boolean isNull() {
		return true;
	}
	
	@Override
	public String toJson() {
		return "null";
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof JsonNullValue;
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}

}
