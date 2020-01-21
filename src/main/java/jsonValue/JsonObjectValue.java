package jsonValue;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class JsonObjectValue extends JsonValue {
	
	private final Collection<JsonObjectPair> v;
	private String toJsonProxy;
	
	protected JsonObjectValue(Collection<? extends JsonObjectPair> v) {
		super();
		
		this.v = Collections.unmodifiableCollection(Objects.requireNonNull(v));
		this.toJsonProxy = null;
	}

	@Override
	public JsonValueType type() {
		return JsonValueType.OBJECT;
	}
	
	@Override
	public Set<JsonString> keySet() {
		return v.stream().map(x -> x.name()).collect(Collectors.toSet());
	}
	
	@Override
	protected Collection<JsonObjectPair> objectPairs() {
		return Collections.unmodifiableCollection(v);
	}
	
	@Override
	public List<JsonValue> values() {
		return v.stream().map(x -> x.value()).collect(Collectors.toList());
	}
	
	@Override
	public void forEach(BiConsumer<JsonString, JsonValue> action) {
		v.stream().forEach(x -> {
			action.accept(x.name(), x.value());
		});
	}
	
	@Override
	public boolean containsKey(CharSequence name) {
		String s = name.toString();
		return v.stream()
				.map(x -> x.name().unescaped())
				.anyMatch(x ->  x.equals(s));
	}
	
	@Override
	public JsonValue get(CharSequence name) {
		return getOrDefault(name, null);
	}
	
	@Override
	public JsonValue getOrDefault(CharSequence name, JsonValue defaultValue) {
		String s = name.toString();
		return v.stream()
				.filter(x -> x.name().unescaped().equals(s))
				.findFirst()
				.map(x -> x.value())
				.orElse(defaultValue);
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
						.map(x -> {
							return JsonStructuralChar.QUOT.str
									+ x.name().escaped()
									+ JsonStructuralChar.QUOT.str
									+ JsonStructuralChar.COLON.str
									+ x.value().toJson();
						})
						.collect(Collectors.joining(
								JsonStructuralChar.COMMA.str,
								JsonStructuralChar.OBJECT_LEFT.str,
								JsonStructuralChar.OBJECT_RIGHT.str));
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
		if ((o != null) && (o instanceof JsonObjectValue)) {
			return ((JsonObjectValue) o).toJson().equals(toJson());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return toJson().hashCode();
	}

}
