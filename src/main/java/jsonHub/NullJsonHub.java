package jsonHub;

import java.io.IOException;
import java.io.Writer;

public class NullJsonHub extends AbstractJsonHub {

	private static final long serialVersionUID = 4404413772323952148L;

	protected NullJsonHub() {
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
		return (o != null) && (o instanceof NullJsonHub);
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}

}
