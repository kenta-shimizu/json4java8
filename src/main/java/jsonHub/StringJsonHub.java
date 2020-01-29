package jsonHub;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import java.util.Optional;

public class StringJsonHub extends AbstractJsonHub {
	
	private static final long serialVersionUID = -873784938882289245L;

	private final JsonString v;
	
	private String toJsonProxy;
	
	protected StringJsonHub(JsonString v) {
		super();
		
		this.v = Objects.requireNonNull(v);
		this.toJsonProxy = null;
	}
	
	@Override
	public JsonHubType type() {
		return JsonHubType.STRING;
	}
	
	@Override
	public Optional<String> optionalString() {
		return Optional.of(v.unescaped());
	}
	
	@Override
	public int length() {
		return v.length();
	}
	
	@Override
	public boolean isEmpty() {
		return v.isEmpty();
	}
	
	@Override
	public String toJson() {
		return toJsonProxy();
	}
	
	@Override
	public void toJson(Writer writer) throws IOException {
		writer.write(toJsonProxy());
	}
	
	private String toJsonProxy() {
		
		synchronized ( this ) {
			
			if ( toJsonProxy == null ) {
				toJsonProxy = JsonStructuralChar.QUOT.str()
						+ v.escaped()
						+ JsonStructuralChar.QUOT.str();
			}
			
			return toJsonProxy;
		}
	}
	
	@Override
	public String toString() {
		return v.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof StringJsonHub)) {
			return ((StringJsonHub) o).toString().equals(toString());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
}