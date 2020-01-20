package jsonValue;

import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class JsonNumberValue extends JsonValue {
	
	private final String v;
	
	protected JsonNumberValue(CharSequence cs) {
		super();
		
		this.v = Objects.requireNonNull(cs).toString();
		if ( this.v.isEmpty() ) {
			throw new JsonValueNumberFormatException("Value is empty");
		}
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.NUMBER;
	}
	
	@Override
	public OptionalInt optionalInt() {
		try {
			return OptionalInt.of(Integer.parseInt(v));
		}
		catch (NumberFormatException e) {
			throw new JsonValueNumberFormatException("parse failed \"" + v + "\"");
		}
	}
	
	@Override
	public OptionalLong optionalLong() {
		try {
			return OptionalLong.of(Long.parseLong(v));
		}
		catch (NumberFormatException e) {
			throw new JsonValueNumberFormatException("parse failed \"" + v + "\"");
		}
	}
	
	
	@Override
	public OptionalDouble optionalDouble() {
		try {
			return OptionalDouble.of(Double.parseDouble(v));
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
		return v;
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof JsonNumberValue)) {
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
