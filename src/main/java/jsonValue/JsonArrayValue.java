package jsonValue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonArrayValue extends JsonValue {
	
	private final List<JsonValue> v;
	private String toJsonProxy;
	
	public JsonArrayValue(List<JsonValue> v) {
		super();
		
		this.v = Collections.unmodifiableList(v);
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
	public String toJson() {
		return toJsonProxy();
	}
	
	private String toJsonProxy() {
		synchronized ( this ) {
			if ( toJsonProxy == null ) {
				toJsonProxy = v.stream()
						.map(x -> x.toJson())
						.collect(Collectors.joining(",", "[", "]"));
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
		if ( o instanceof JsonArrayValue ) {
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
