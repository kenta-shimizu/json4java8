package jsonHub;

import java.io.IOException;
import java.io.Writer;

public class JsonNullHub extends JsonHub {

	protected JsonNullHub() {
		super();
	}
	
	@Override
	public JsonHubType type() {
		return JsonHubType.NULL;
	}
	
	@Override
	public void toJson(Writer writer) throws IOException {
		writer.write(JsonLiteral.NULL.toString());
	}
	
	@Override
	public String toString() {
		return JsonLiteral.NULL.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o instanceof JsonNullHub);
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}

}
