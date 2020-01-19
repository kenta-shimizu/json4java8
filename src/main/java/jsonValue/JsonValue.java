package jsonValue;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Immutable Object
 *
 */
abstract public class JsonValue implements Iterable<JsonValue> {
	
	public JsonValue() {
		/* Nothing */
	}
	
	/**
	 * enable if type is ARRAY
	 * 
	 * @throws JsonValueUnsupportedOperationException
	 */
	@Override
	public Iterator<JsonValue> iterator() {
		throw new JsonValueUnsupportedOperationException(type() + " not support #iterator");
	}
	
	abstract public JsonValueType type();
	
	/**
	 * enable if type is ARRAY
	 * 
	 * @return Array values stream
	 * @throws JsonValueUnsupportedOperationException
	 */
	public Stream<JsonValue> stream() {
		throw new JsonValueUnsupportedOperationException(type() + " not support #stream");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @return names
	 * @throws JsonValueUnsupportedOperationException
	 */
	public Set<JsonString> keySet() {
		throw new JsonValueUnsupportedOperationException(type() + " not support #keySet");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @return pairs
	 * @throws JsonValueUnsupportedOperationException
	 */
	protected Collection<JsonObjectPair> objectPairs() {
		throw new JsonValueUnsupportedOperationException(type() + " not support #objectPairs");
	}
	
	/**
	 * enable if type is OBJECT or ARRAY
	 * 
	 * @return values
	 * @throws JsonValueUnsupportedOperationException
	 */
	public List<JsonValue> values() {
		throw new JsonValueUnsupportedOperationException(type() + " not support #values");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param consumer<NAME, VALUE>
	 * @throws JsonValueUnsupportedOperationException
	 */
	public void forEach(BiConsumer<JsonString, JsonValue> consumer) {
		throw new JsonValueUnsupportedOperationException(type() + " not support #forEach");
	}
	
	/**
	 * enable if type is ARRAY
	 *  
	 * @param index
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public JsonValue get(int index) {
		throw new JsonValueUnsupportedOperationException(type() + " not support #get(" + index + ")");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @return true if has name
	 * @throws JsonValueUnsupportedOperationException
	 */
	public boolean containsKey(CharSequence name) {
		throw new JsonValueUnsupportedOperationException(type() + " not support #containsKey(\"" + name + "\")");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @return value. null if not has name.
	 * @throws JsonValueUnsupportedOperationException
	 */
	public JsonValue get(CharSequence name) {
		throw new JsonValueUnsupportedOperationException(type() + "not support #get(\"" + name + "\")");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @param defaultValue
	 * @return JsonValue
	 * @throws JsonValueUnsupportedOperationException
	 */
	public JsonValue getOrDefault(CharSequence name, JsonValue defaultValue) {
		throw new JsonValueUnsupportedOperationException(type() + "not support #getOrDefault");
	}
	
	/**
	 * enable if type is OBJECT-chains
	 * 
	 * @param names
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public JsonValue get(String... names) {
		return _get(new LinkedList<>(Arrays.asList(names)));
	}
	
	private JsonValue _get(LinkedList<String> ll) {
		
		if ( ll.isEmpty() ) {
			return this;
		} else {
			String s = ll.removeFirst();
			return get(s)._get(ll);
		}
	}
	
	/**
	 * enable if STRING or ARRAY or OBJECT
	 * 
	 * @return length
	 * @throws JsonValueUnsupportedOperationException
	 */
	public int length() {
		throw new JsonValueUnsupportedOperationException(type() + " not support #length");
	}
	
	/**
	 * enable if STRING or ARRAY or OBJECT
	 * 
	 * @return true if empty
	 * @throws JsonValueUnsupportedOperationException
	 */
	public boolean isEmpty() {
		throw new JsonValueUnsupportedOperationException(type() + " not support #isEmpty");
	}
	
	/**
	 * 
	 * @return true if value is null
	 */
	public boolean isNull() {
		return type() == JsonValueType.NULL;
	}
	
	/**
	 * 
	 * @return true if value is not null
	 */
	public boolean notNull() {
		return ! isNull();
	}
	
	public boolean isTrue() {
		return type() == JsonValueType.TRUE;
	}
	
	public boolean isFalse() {
		return type() == JsonValueType.FALSE;
	}
	
	public boolean isString() {
		return type() == JsonValueType.STRING;
	}
	
	public boolean isNumber() {
		return type() == JsonValueType.NUMBER;
	}
	
	public boolean isArray() {
		return type() == JsonValueType.ARRAY;
	}
	
	public boolean isObject() {
		return type() == JsonValueType.OBJECT;
	}
	
	public Optional<Boolean> optionalBoolean() {
		return Optional.empty();
	}
	
	public OptionalInt optionalInt() {
		return OptionalInt.empty();
	}
	
	public OptionalLong optionalLong() {
		return OptionalLong.empty();
	}
	
	public OptionalDouble optionalDouble() {
		return OptionalDouble.empty();
	}
	
	public Optional<String> optionalString() {
		return Optional.empty();
	}
	
	
	/**
	 * enable if type is TRUE or FALSE
	 * 
	 * @return boolean
	 * @throws JsonValueUnsupportedOperationException
	 */
	public boolean booleanValue() {
		return optionalBoolean().orElseThrow(() -> new JsonValueUnsupportedOperationException(type() + " not support #booleanValue"));
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public int intValue() {
		return optionalInt().orElseThrow(() -> new JsonValueUnsupportedOperationException(type() + " not support #intValue"));
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public long longValue() {
		return optionalLong().orElseThrow(() -> new JsonValueUnsupportedOperationException(type() + " not support #longValue"));
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public double doubleValue() {
		return optionalDouble().orElseThrow(() -> new JsonValueUnsupportedOperationException(type() + " not support #doubleValue"));
	}
	
	
	/* builders */
	
	/**
	 * parse to JsonValue
	 * 
	 * @param json
	 * @return JsonValue
	 */
	public static JsonValue fromJson(CharSequence json) {
		return JsonValueParser.getInstance().parse(json);
	}
	
	/**
	 * parse to JaonValue
	 * 
	 * @param reader
	 * @return JsonValue
	 * @throws IOException
	 */
	public static JsonValue fromJson(Reader reader) throws IOException {
		return JsonValueParser.getInstance().parse(reader);
	}
	
	/**
	 * parse to minimun Json String
	 * 
	 * @return json
	 */
	abstract public String toJson();
	
	
	/**
	 * 
	 * @return Pretty-Print-JSON
	 */
	public String prettyPrint() {
		return new JsonValuePrettyPrinter().print(this);
	}
	
	/**
	 * 
	 * @param config
	 * @return Pretty-Print-JSON
	 */
	public String prettyPrint(JsonValuePrettyPrinterConfig config) {
		return new JsonValuePrettyPrinter(config).print(this);
	}
	
	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void prettyPrint(Writer writer) throws IOException {
		new JsonValuePrettyPrinter().print(this, writer);
	}
	
	public void prettyPrint(Writer writer, JsonValuePrettyPrinterConfig config) throws IOException {
		new JsonValuePrettyPrinter(config).print(this, writer);
	}
	
}
