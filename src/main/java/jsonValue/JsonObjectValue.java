package jsonValue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class JsonObjectValue extends JsonValue {
	
	private final Collection<JsonObjectPair> v;
	private String toJsonProxy;
	
	public JsonObjectValue(Collection<? extends JsonObjectPair> v) {
		super();
		
		this.v = Collections.unmodifiableCollection(v);
		this.toJsonProxy = null;
	}

	@Override
	public JsonValueType type() {
		return JsonValueType.OBJECT;
	}
	
	@Override
	public Set<String> keySet() {
		return v.stream().map(x -> x.name()).collect(Collectors.toSet());
	}
	
	@Override
	public List<JsonValue> values() {
		return v.stream().map(x -> x.value()).collect(Collectors.toList());
	}
	
	@Override
	public void forEach(BiConsumer<String, JsonValue> consumer) {
		v.stream().forEach(x -> {
			consumer.accept(x.name(), x.value());
		});
	}
	
	@Override
	public boolean containsKey(CharSequence name) {
		String s = name.toString();
		return v.stream()
				.map(x -> x.name())
				.anyMatch(x ->  x.equals(s));
	}
	
	@Override
	public JsonValue get(CharSequence name) {
		String s = name.toString();
		return v.stream()
				.filter(x -> x.name().equals(s))
				.findFirst()
				.map(x -> x.value())
				.orElse(null);
	}
	
	@Override
	public String toJson() {
		return toJsonProxy();
	}
	
	private String toJsonProxy() {
		synchronized ( this ) {
			if ( toJsonProxy == null ) {
				toJsonProxy = v.stream()
						.map(x -> {
							return "\"" + escape(x.name()) + "\":" + x.value().toJson();
						})
						.collect(Collectors.joining(",", "{", "}"));
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
		if ( o instanceof JsonObjectValue ) {
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
