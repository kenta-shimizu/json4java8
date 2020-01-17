package jsonValue;

public class JsonTrueValue extends JsonValue {
	
	public JsonTrueValue() {
		super();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.TRUE;
	}
	
	@Override
	public boolean booleanValue() {
		return true;
	}
	
	@Override
	public String toJson() {
		return "true";
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof JsonTrueValue;
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}
	
}
