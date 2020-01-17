package jsonValue;

public class JsonNumberValue extends JsonValue {
	
	private final String v;
	
	public JsonNumberValue(CharSequence cs) {
		super();
		
		this.v = cs.toString();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.NUMBER;
	}
	
	@Override
	public int intValue() {
		try {
			return Integer.parseInt(v);
		}
		catch (NumberFormatException e) {
			throw new JsonValueNumberFormatException("parse failed \"" + v + "\"");
		}
	}
	
	@Override
	public long longValue() {
		try {
			return Long.parseLong(v);
		}
		catch (NumberFormatException e) {
			throw new JsonValueNumberFormatException("parse failed \"" + v + "\"");
		}
	}
	
	@Override
	public float floatValue() {
		try {
			return Float.parseFloat(v);
		}
		catch (NumberFormatException e) {
			throw new JsonValueNumberFormatException("parse failed \"" + v + "\"");
		}
	}
	
	@Override
	public double doubleValue() {
		try {
			return Double.parseDouble(v);
		}
		catch (NumberFormatException e) {
			throw new JsonValueNumberFormatException("parse failed \"" + v + "\"");
		}
	}
	
	@Override
	public String toJson() {
		return v;
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof JsonNumberValue ) {
			return ((JsonNumberValue) o).v.equals(v);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return v.hashCode();
	}
	
}
