package jsonValue;

public class JsonNullValue extends JsonValue {

	protected JsonNullValue() {
		super();
	}

	@Override
	public JsonValueType type() {
		return JsonValueType.NULL;
	}
	
	@Override
	public String toJson() {
		return "null";
	}
	
	@Override
	public String toString() {
		return "null";
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o instanceof JsonNullValue);
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}

}
