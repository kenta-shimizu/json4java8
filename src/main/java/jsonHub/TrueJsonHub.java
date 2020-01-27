package jsonHub;

import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

public class TrueJsonHub extends AbstractJsonHub {
	
	private static final long serialVersionUID = -788713372914256563L;
	
	protected TrueJsonHub() {
		super();
	}
	
	@Override
	public JsonHubType type() {
		return JsonHubType.TRUE;
	}
	
	@Override
	public Optional<Boolean> optionalBoolean() {
		return Optional.of(Boolean.TRUE);
	}
	
	@Override
	public void toJson(Writer writer) throws IOException {
		writer.write(JsonLiteral.TRUE.toString());
	}
	
	@Override
	public String toString() {
		return Boolean.TRUE.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o != null) && (o instanceof TrueJsonHub);
	}
	
	@Override
	public int hashCode() {
		return type().hashCode();
	}
	
}
