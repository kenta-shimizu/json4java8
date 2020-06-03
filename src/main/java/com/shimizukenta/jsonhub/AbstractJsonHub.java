package com.shimizukenta.jsonhub;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Immutable Object
 *
 */
abstract public class AbstractJsonHub implements JsonHub, Serializable {
	
	private static final long serialVersionUID = -8854276210327173340L;
	
	private byte[] toBytesCache;
	private byte[] toBytesExcludeNullValueInObjectCache;
	
	public AbstractJsonHub() {
		toBytesCache = null;
		toBytesExcludeNullValueInObjectCache = null;
	}
	
	private byte[] toBytesCache() {
		
		synchronized ( this ) {
			
			if ( toBytesCache == null ) {
				toBytesCache = toJson().getBytes(StandardCharsets.UTF_8);;
			}
			
			return toBytesCache;
		}
	}
	
	private byte[] toBytesExcludeNullValueInObjectCache() {
		
		synchronized ( this ) {
			
			if ( toBytesExcludeNullValueInObjectCache == null ) {
				toBytesExcludeNullValueInObjectCache = toJsonExcludedNullValueInObject().getBytes(StandardCharsets.UTF_8);
			}
			
			return toBytesExcludeNullValueInObjectCache;
		}
	}
	
	@Override
	public Iterator<JsonHub> iterator() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #iterator");
	}
	
	@Override
	public Spliterator<JsonHub> spliterator() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #spliterator");
	}
	
	@Override
	public void forEach(Consumer<? super JsonHub> action) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #forEach");
	}
	
	@Override
	public void forEach(BiConsumer<? super JsonString, ? super JsonHub> action) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #forEach");
	}
	
	@Override
	public Stream<JsonHub> stream() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #stream");
	}
	
	@Override
	public Set<JsonString> keySet() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #keySet");
	}
	
	@Override
	public List<JsonHub> values() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #values");
	}
	
	/**
	 * available if type is ARRAY
	 *  
	 * @param index
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(int index) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #get(" + index + ")");
	}
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return true if has name
	 * @throws JsonHubUnsupportedOperationException
	 */
	public boolean containsKey(CharSequence name) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #containsKey(\"" + name + "\")");
	}
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return value. null if not has name.
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(CharSequence name) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #get(\"" + name + "\")");
	}
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return emptyObject() if not exist
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub getOrDefault(CharSequence name) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #getOrDefault");
	}
	
	/**
	 * available if type is OBJECT
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
	 * available if type is OBJECT-chains
	 * 
	 * @param names
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(String... names) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #get(\"name\"...)");
	}
	
	/**
	 * available if STRING or ARRAY or OBJECT
	 * 
	 * @return length
	 * @throws JsonHubUnsupportedOperationException
	 */
	public int length() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #length");
	}
	
	/**
	 * available if STRING or ARRAY or OBJECT
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
	public boolean nonNull() {
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
	 * available if type is TRUE or FALSE
	 * 
	 * @return boolean
	 * @throws JsonHubUnsupportedOperationException
	 */
	public boolean booleanValue() {
		return optionalBoolean().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #booleanValue"));
	}
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public int intValue() {
		return optionalInt().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #intValue"));
	}
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public long longValue() {
		return optionalLong().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #longValue"));
	}
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public double doubleValue() {
		return optionalDouble().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #doubleValue"));
	}
	
	
	/**
	 * parse to compact-JSON-String
	 * 
	 * @return json
	 */
	public String toJson() {
		return JsonHubCompactPrettyPrinter.getInstance().print(this);
	}
	
	/**
	 * compact-JSON-String to writer
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void toJson(Writer writer) throws IOException {
		JsonHubCompactPrettyPrinter.getInstance().print(this, writer);
	}
	
	/**
	 * parse to compact-JSON-String exclued null value in Object;
	 * 
	 * @return json of excluded null value in Object.
	 */
	public String toJsonExcludedNullValueInObject() {
		return JsonHubNoneNullValueInObjectCompactPrettyPrinter.getInstance().print(this);
	}
	
	/**
	 * parse to compact-JSON-String exclued null value in Object;
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void toJsonExcludedNullValueInObject(Writer writer) throws IOException {
		JsonHubNoneNullValueInObjectCompactPrettyPrinter.getInstance().print(this, writer);
	}
	
	/**
	 * write to file
	 * 
	 * @param file-path
	 * @throws IOException
	 */
	public void writeFile(Path path) throws IOException {
		
		try (
				BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
				) {
			
			toJson(bw);
		}
	}
	
	/**
	 * write to file
	 * 
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	public void writeFile(Path path, OpenOption... options) throws IOException {
		
		try (
				BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, options);
				) {
			
			toJson(bw);
		}
	}
	
	
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
	 * @return Pretty-Print-JSON with config format
	 */
	public String prettyPrint(JsonHubPrettyPrinterConfig config) {
		return new JsonHubPrettyPrinter(config).print(this);
	}
	
	/**
	 * write Pretty-Print-JSON to writer
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void prettyPrint(Writer writer) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, writer);
	}
	
	/**
	 * write Pretty-Print-JSON to writer with config format
	 * 
	 * @param writer
	 * @param config
	 * @throws IOException
	 */
	public void prettyPrint(Writer writer, JsonHubPrettyPrinterConfig config) throws IOException {
		new JsonHubPrettyPrinter(config).print(this, writer);
	}
	
	/**
	 * write Pretty-Print-JSON to File
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void prettyPrint(Path path) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, path);
	}
	
	/**
	 * write Pretty-Print-JSON to File
	 * 
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	public void prettyPrint(Path path, OpenOption... options) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, path, options);
	}
	
	/**
	 * write Pretty-Print-JSON to File with config format
	 * 
	 * @param path
	 * @param config
	 * @throws IOException
	 */
	public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config) throws IOException {
		new JsonHubPrettyPrinter(config).print(this, path);
	}
	
	/**
	 * write Pretty-Print-JSON to File with config format
	 * 
	 * @param path
	 * @param config
	 * @param options
	 * @throws IOException
	 */
	public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config, OpenOption... options) throws IOException {
		new JsonHubPrettyPrinter(config).print(this, path, options);
	}
	
	
	/**
	 * 
	 * @param <T>
	 * @param classOfT
	 * @return Pojo
	 * @throws JsonHubParseException
	 */
	public <T> T toPojo(Class<T> classOfT) {
		return JsonHubToPojoParser.getInstance().toPojo(this, classOfT);
	}
	
	@Override
	public byte[] getBytes() {
		byte[] bs = toBytesCache();
		return Arrays.copyOf(bs, bs.length);
	}
	
	@Override
	public void writeBytes(OutputStream strm) throws IOException {
		strm.write(toBytesCache());
	}
	
	@Override
	public byte[] getBytesExcludedNullValueInObject() {
		byte[] bs = this.toBytesExcludeNullValueInObjectCache();
		return Arrays.copyOf(bs, bs.length);
	}
	
	@Override
	public void writeBytesExcludedNullValueInObject(OutputStream strm) throws IOException {
		strm.write(toBytesExcludeNullValueInObjectCache());
	}
	

	
}
