package jsonValue;

import java.io.IOException;
import java.io.Writer;

public class JsonNullValue extends JsonValue {

	protected JsonNullValue() {
		super();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.NULL;
	}
	
	@Override
	public void toJson(Writer writer) throws IOException {
		writer.write(JsonLiteral.NULL.toString());
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
