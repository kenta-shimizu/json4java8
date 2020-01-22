package jsonHub;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonArrayHub extends JsonHub {
	
	private final List<JsonHub> v;
	private String toJsonProxy;
	
	public JsonArrayHub(List<? extends JsonHub> v) {
		super();
		
		this.v = Collections.unmodifiableList(Objects.requireNonNull(v));
		this.toJsonProxy = null;
	}

	@Override
	public Iterator<JsonHub> iterator() {
		return v.iterator();
	}
	
	@Override
	public JsonHubType type() {
		return JsonHubType.ARRAY;
	}
	
	@Override
	public Stream<JsonHub> stream() {
		return v.stream();
	}
	
	@Override
	public List<JsonHub> values() {
		return Collections.unmodifiableList(v);
	}
	
	@Override
	public JsonHub get(int index) {
		
		try {
			return v.get(index);
		}
		catch ( IndexOutOfBoundsException e ) {
			throw new JsonHubIndexOutOfBoundsException("get: " + index);
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
								JsonStructuralChar.SEPARATOR_VALUE.str(),
								JsonStructuralChar.ARRAY_BIGIN.str(),
								JsonStructuralChar.ARRAY_END.str()));
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
		if ((o != null) && (o instanceof JsonArrayHub)) {
			return ((JsonArrayHub) o).toJson().equals(toJson());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toJson().hashCode();
	}
	
}
