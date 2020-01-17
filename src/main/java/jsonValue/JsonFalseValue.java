package jsonValue;

public class JsonFalseValue extends JsonValue {

	public JsonFalseValue() {
		super();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.FALSE;
	}
	
	@Override
	public boolean booleanValue() {
		return false;
	}
	
	@Override
	public String toJson() {
		return "false";
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof JsonFalseValue;
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}
	
}
