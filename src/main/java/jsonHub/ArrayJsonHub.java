package jsonHub;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayJsonHub extends AbstractJsonHub {
	
	private static final long serialVersionUID = -9051930871585233794L;
	
	private final List<JsonHub> v;
	private String toJsonProxy;
	
	protected ArrayJsonHub(List<? extends JsonHub> v) {
		super();
		
		this.v = new ArrayList<>(Objects.requireNonNull(v));
		this.toJsonProxy = null;
	}
	
	@Override
	public Iterator<JsonHub> iterator() {
		return v.iterator();
	}
	
	@Override
	public Spliterator<JsonHub> spliterator() {
		return v.spliterator();
	}
	
	@Override
	public void forEach(Consumer<? super JsonHub> action) {
		v.forEach(action);
	}
	
	@Override
	public void forEach(BiConsumer<? super JsonString, ? super JsonHub> action) {
		v.forEach(x -> {
			action.accept(null, x);
		});
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
		if ((o != null) && (o instanceof ArrayJsonHub)) {
			return ((ArrayJsonHub) o).toJson().equals(toJson());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toJson().hashCode();
	}
	
}
