package jsonValue;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonArrayValue extends JsonValue {
	
	private final List<JsonValue> v;
	private String toJsonProxy;
	
	public JsonArrayValue(List<? extends JsonValue> v) {
		super();
		
		this.v = Collections.unmodifiableList(Objects.requireNonNull(v));
		this.toJsonProxy = null;
	}

	@Override
	public Iterator<JsonValue> iterator() {
		return v.iterator();
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.ARRAY;
	}
	
	@Override
	public Stream<JsonValue> stream() {
		return v.stream();
	}
	
	@Override
	public List<JsonValue> values() {
		return Collections.unmodifiableList(v);
	}
	
	@Override
	public JsonValue get(int index) {
		
		try {
			return v.get(index);
		}
		catch ( IndexOutOfBoundsException e ) {
			throw new JsonValueIndexOutOfBoundsException("get: " + index);
		}
	}
	
	@Override
	public int length() {
		return v.size();
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
				toJsonProxy = v.stream()
						.map(x -> x.toJson())
						.collect(Collectors.joining(
								JsonStructuralChar.COMMA.str,
								JsonStructuralChar.ARRAY_LEFT.str,
								JsonStructuralChar.ARRAY_RIGHT.str));
			}
			
			return toJsonProxy;
		}
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof JsonArrayValue)) {
			return ((JsonArrayValue) o).toJson().equals(toJson());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toJson().hashCode();
	}
	
}
