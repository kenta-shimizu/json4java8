package jsonValue;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class JsonNumberValue extends JsonValue {
	
	private final Number num;
	private final String str;
	
	protected JsonNumberValue(CharSequence cs) {
		super();
		
		this.str = Objects.requireNonNull(cs).toString();
		
		try {
			if ( this.str.contains(".") ) {
				this.num = Double.valueOf(this.str);
			} else {
				this.num = Long.valueOf(this.str);
			}
		}
		catch ( NumberFormatException e ) {
			throw new JsonValueNumberFormatException(e.getMessage());
		}
	}
	
	protected JsonNumberValue(Number num) {
		super();
		
		this.num = Objects.requireNonNull(num);
		this.str = this.num.toString();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.NUMBER;
	}
	
	@Override
	public OptionalInt optionalInt() {
		return OptionalInt.of(num.intValue());
	}
	
	@Override
	public OptionalLong optionalLong() {
		return OptionalLong.of(num.longValue());
	}
	
	@Override
	public OptionalDouble optionalDouble() {
		return OptionalDouble.of(num.doubleValue());
	}
	
	@Override
	public Optional<Number> optionalNubmer() {
		return Optional.of(num);
	}
	
	@Override
	public String toJson() {
		return str;
	}
	
	@Override
	public void toJson(Writer writer) throws IOException {
		writer.write(str);
	}
	
	@Override
	public String toString() {
		return str;
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof JsonNumberValue)) {
			return ((JsonNumberValue) o).num.equals(num);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return num.hashCode();
	}
	
}
