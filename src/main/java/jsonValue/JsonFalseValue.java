package jsonValue;

import java.io.IOException;
import java.io.Writer;
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
	public void toJson(Writer writer) throws IOException {
		writer.write(JsonLiteral.FALSE.toString());
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
