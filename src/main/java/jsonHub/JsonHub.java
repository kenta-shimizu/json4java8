package jsonHub;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
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
abstract public class JsonHub implements Iterable<JsonHub> {
	
	public JsonHub() {
		/* Nothing */
	}
	
	/**
	 * enable if type is ARRAY
	 * 
	 * @throws JsonHubUnsupportedOperationException
	 */
	@Override
	public Iterator<JsonHub> iterator() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #iterator");
	}
	
	abstract public JsonHubType type();
	
	/**
	 * enable if type is ARRAY
	 * 
	 * @return Array values stream
	 * @throws JsonHubUnsupportedOperationException
	 */
	public Stream<JsonHub> stream() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #stream");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @return names
	 * @throws JsonHubUnsupportedOperationException
	 */
	public Set<JsonString> keySet() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #keySet");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @return pairs
	 * @throws JsonHubUnsupportedOperationException
	 */
	protected Collection<JsonObjectPair> objectPairs() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #objectPairs");
	}
	
	/**
	 * enable if type is OBJECT or ARRAY
	 * 
	 * @return values
	 * @throws JsonHubUnsupportedOperationException
	 */
	public List<JsonHub> values() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #values");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param consumer<NAME, VALUE>
	 * @throws JsonHubUnsupportedOperationException
	 */
	public void forEach(BiConsumer<JsonString, JsonHub> action) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #forEach");
	}
	
	/**
	 * enable if type is ARRAY
	 *  
	 * @param index
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(int index) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #get(" + index + ")");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @return true if has name
	 * @throws JsonHubUnsupportedOperationException
	 */
	public boolean containsKey(CharSequence name) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #containsKey(\"" + name + "\")");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @return value. null if not has name.
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(CharSequence name) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #get(\"" + name + "\")");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @param defaultValue
	 * @return JsonHub
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub getOrDefault(CharSequence name, JsonHub defaultValue) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #getOrDefault");
	}
	
	/**
	 * enable if type is OBJECT-chains
	 * 
	 * @param names
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(String... names) {
		return _get(new LinkedList<>(Arrays.asList(names)));
	}
	
	private JsonHub _get(LinkedList<String> ll) {
		
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
	 * @throws JsonHubUnsupportedOperationException
	 */
	public int length() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #length");
	}
	
	/**
	 * enable if STRING or ARRAY or OBJECT
	 * 
	 * @return true if empty
	 * @throws JsonHubUnsupportedOperationException
	 */
	public boolean isEmpty() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #isEmpty");
	}
	
	/**
	 * 
	 * @return true if value is null
	 */
	public boolean isNull() {
		return type() == JsonHubType.NULL;
	}
	
	/**
	 * 
	 * @return true if value is not null
	 */
	public boolean notNull() {
		return ! isNull();
	}
	
	public boolean isTrue() {
		return type() == JsonHubType.TRUE;
	}
	
	public boolean isFalse() {
		return type() == JsonHubType.FALSE;
	}
	
	public boolean isString() {
		return type() == JsonHubType.STRING;
	}
	
	public boolean isNumber() {
		return type() == JsonHubType.NUMBER;
	}
	
	public boolean isArray() {
		return type() == JsonHubType.ARRAY;
	}
	
	public boolean isObject() {
		return type() == JsonHubType.OBJECT;
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
	
	public Optional<Number> optionalNubmer() {
		return Optional.empty();
	}
	
	/**
	 * enable if type is TRUE or FALSE
	 * 
	 * @return boolean
	 * @throws JsonHubUnsupportedOperationException
	 */
	public boolean booleanValue() {
		return optionalBoolean().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #booleanValue"));
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public int intValue() {
		return optionalInt().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #intValue"));
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public long longValue() {
		return optionalLong().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #longValue"));
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public double doubleValue() {
		return optionalDouble().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #doubleValue"));
	}
	
	
	/* builders */
	
	/**
	 * parse to JsonHub
	 * 
	 * @param json
	 * @return JsonHub
	 */
	public static JsonHub fromJson(CharSequence json) {
		return JsonHubJsonParser.getInstance().parse(json);
	}
	
	/**
	 * parse to JaonValue
	 * 
	 * @param reader
	 * @return JsonHub
	 * @throws IOException
	 */
	public static JsonHub fromJson(Reader reader) throws IOException {
		return JsonHubJsonParser.getInstance().parse(reader);
	}
	
	/**
	 * parse to minimun Json String
	 * 
	 * @return json
	 */
	public String toJson() {
		
		try (
				StringWriter sw = new StringWriter();
				) {
			toJson(sw);
			return sw.toString();
		}
		catch (IOException notHappen) {
			throw new RuntimeException(notHappen);
		}
	}
	
	/**
	 * 
	 * @param writer
	 * @return
	 */
	abstract public void toJson(Writer writer) throws IOException;
	
	
	/**
	 * 
	 * @return Pretty-Print-JSON
	 */
	public String prettyPrint() {
		return JsonHubPrettyPrinter.getDefaultPrinter().print(this);
	}
	
	/**
	 * 
	 * @param config
	 * @return Pretty-Print-JSON
	 */
	public String prettyPrint(JsonHubPrettyPrinterConfig config) {
		return new JsonHubPrettyPrinter(config).print(this);
	}
	
	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void prettyPrint(Writer writer) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, writer);
	}
	
	public void prettyPrint(Writer writer, JsonHubPrettyPrinterConfig config) throws IOException {
		new JsonHubPrettyPrinter(config).print(this, writer);
	}
	
	public static JsonHub fromPojo(Object pojo) {
		return JsonHubPojoParser.getInstance().fromPojo(pojo);
	}
	
	public <T> T toPojo(Class<T> classOfT) {
		return JsonHubPojoParser.getInstance().toPojo(this, classOfT);
	}
	
}
