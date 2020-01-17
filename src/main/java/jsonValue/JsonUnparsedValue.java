package jsonValue;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class JsonUnparsedValue extends JsonValue {
	
	private final String v;
	private JsonValue parsed;
	
	public JsonUnparsedValue(CharSequence cs) {
		super();
		
		this.v = cs.toString();
		this.parsed = null;
	}
	
	@Override
	public Iterator<JsonValue> iterator() {
		return parse().iterator();
	}
	
	@Override
	public JsonValueType type() {
		return parse().type();
	}
	
	public Stream<JsonValue> stream() {
		return parse().stream();
	}
	
	public Set<String> keySet() {
		return parse().keySet();
	}
	
	@Override
	public List<JsonValue> values() {
		return parse().values();
	}
	
	@Override
	public void forEach(BiConsumer<String, JsonValue> consumer) {
		parse().forEach(consumer);
	}
	
	@Override
	public JsonValue get(int index) {
		return parse().get(index);
	}
	
	@Override
	public boolean containsKey(CharSequence name) {
		return parse().containsKey(name);
	}
	
	@Override
	public JsonValue get(CharSequence name) {
		return parse().get(name);
	}
	
	@Override
	public int length() {
		return parse().length();
	}
	
	@Override
	public boolean isNull() {
		return parse().isNull();
	}
	
	@Override
	public boolean booleanValue() {
		return parse().booleanValue();
	}
	
	@Override
	public int intValue() {
		return parse().intValue();
	}
	
	@Override
	public long longValue() {
		return parse().longValue();
	}
	
	@Override
	public float floatValue() {
		return parse().floatValue();
	}
	
	@Override
	public double doubleValue() {
		return parse().doubleValue();
	}
	
	@Override
	public String toJson() {
		return parse().toJson();
	}
	
	@Override
	public String toString() {
		return v;
	}
	
	@Override
	public boolean equals(Object o) {
		return parse().equals(o);
	}
	
	@Override
	public int hashCode() {
		return parse().hashCode();
	}
	
	
	private JsonValue parse() {
		
		synchronized ( this ) {
			
			if ( parsed == null ) {
				this.parsed = JsonValueParser.getInstance().parse(v);
			}
			
			return parsed;
		}
	}
}
