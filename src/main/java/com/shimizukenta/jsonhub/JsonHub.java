package com.shimizukenta.jsonhub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
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

public interface JsonHub extends Iterable<JsonHub> {
	
	/**
	 * available if type is ARRAY or OBJECT
	 * if type is OBJECT, VALUE is JsonHub.
	 * 
	 * @throws JsonHubUnsupportedOperationException
	 */
	@Override
	default public Iterator<JsonHub> iterator() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #iterator");
	}
	
	/**
	 * available if type is ARRAY or OBJECT
	 * if type is OBJECT, VALUE is JsonHub.
	 * 
	 * @throws JsonHubUnsupportedOperationException
	 */
	@Override
	default public Spliterator<JsonHub> spliterator() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #spliterator");
	}
	
	/**
	 * available if type is OBJECT or ARRAY.<br />
	 * if type is OBJECT, VALUE is JsonHub.
	 * 
	 * @param Consumer<VALUE>
	 * @throws JsonHubUnsupportedOperationException
	 */
	@Override
	default public void forEach(Consumer<? super JsonHub> action) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #forEach");
	}
	
	/**
	 * available if type is OBJECT or ARRAY.<br />
	 * if type is ARRAY, NAME is null.
	 * 
	 * @param BiConsumer<NAME, VALUE>
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public void forEach(BiConsumer<? super JsonString, ? super JsonHub> action) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #forEach");
	}
	
	
	public JsonHubType type();
	
	/**
	 * available if type is OBJECT or ARRAY.<br />
	 * if type is OBJECT, VALUE is JsonHub.
	 * 
	 * @return Array values stream
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public Stream<JsonHub> stream() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #stream");
	}
	
	/**
	 * available if type is OBJECT
	 * 
	 * @return names
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public Set<JsonString> keySet() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #keySet");
	}
	
	/**
	 * available if type is OBJECT or ARRAY
	 * 
	 * @return values
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public List<JsonHub> values() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #values");
	}
	
	/**
	 * available if type is ARRAY
	 *  
	 * @param index
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public JsonHub get(int index) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #get(" + index + ")");
	}
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return true if has name
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public boolean containsKey(CharSequence name) {
		throw new JsonHubUnsupportedOperationException(type() + " not support #containsKey(\"" + name + "\")");
	}
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return value. null if not has name.
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public JsonHub get(CharSequence name) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #get(\"" + name + "\")");
	}
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return emptyObject() if not exist
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public JsonHub getOrDefault(CharSequence name) {
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
	default public JsonHub getOrDefault(CharSequence name, JsonHub defaultValue) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #getOrDefault");
	}
	
	/**
	 * available if type is OBJECT-chains
	 * 
	 * @param names
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public JsonHub get(String... names) {
		throw new JsonHubUnsupportedOperationException(type() + "not support #get(\"name\"...)");
	}
	
	/**
	 * available if STRING or ARRAY or OBJECT
	 * 
	 * @return length
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public int length() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #length");
	}
	
	/**
	 * available if STRING or ARRAY or OBJECT
	 * 
	 * @return true if empty
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public boolean isEmpty() {
		throw new JsonHubUnsupportedOperationException(type() + " not support #isEmpty");
	}
	
	/**
	 * 
	 * @return true if value is null
	 */
	default public boolean isNull() {
		return type() == JsonHubType.NULL;
	}
	
	/**
	 * 
	 * @return true if value is not null
	 */
	default public boolean nonNull() {
		return ! isNull();
	}
	
	default public boolean isTrue() {
		return type() == JsonHubType.TRUE;
	}
	
	default public boolean isFalse() {
		return type() == JsonHubType.FALSE;
	}
	
	default public boolean isString() {
		return type() == JsonHubType.STRING;
	}
	
	default public boolean isNumber() {
		return type() == JsonHubType.NUMBER;
	}
	
	default public boolean isArray() {
		return type() == JsonHubType.ARRAY;
	}
	
	default public boolean isObject() {
		return type() == JsonHubType.OBJECT;
	}
	
	default public Optional<Boolean> optionalBoolean() {
		return Optional.empty();
	}
	
	default public OptionalInt optionalInt() {
		return OptionalInt.empty();
	}
	
	default public OptionalLong optionalLong() {
		return OptionalLong.empty();
	}
	
	default public OptionalDouble optionalDouble() {
		return OptionalDouble.empty();
	}
	
	default public Optional<String> optionalString() {
		return Optional.empty();
	}
	
	default public Optional<Number> optionalNubmer() {
		return Optional.empty();
	}
	
	/**
	 * available if type is TRUE or FALSE
	 * 
	 * @return boolean
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public boolean booleanValue() {
		return optionalBoolean().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #booleanValue"));
	}
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public int intValue() {
		return optionalInt().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #intValue"));
	}
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public long longValue() {
		return optionalLong().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #longValue"));
	}
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	default public double doubleValue() {
		return optionalDouble().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #doubleValue"));
	}
	
	
	/* builders */
	
	public static JsonHubBuilder getBuilder() {
		return JsonHubBuilder.getInstance();
	}
	
	/**
	 * parse to JsonHub
	 * 
	 * @param json
	 * @return JsonHub
	 * @throws JsonHubParseException
	 */
	public static JsonHub fromJson(CharSequence json) {
		return JsonHubJsonParser.getInstance().parse(json);
	}
	
	/**
	 * parse to JaonHub
	 * 
	 * @param reader
	 * @return JsonHub
	 * @throws IOException
	 * @throws JsonHubParseException
	 */
	public static JsonHub fromJson(Reader reader) throws IOException {
		return JsonHubJsonParser.getInstance().parse(reader);
	}
	
	/**
	 * parse to compact-JSON-String
	 * 
	 * @return json
	 */
	default public String toJson() {
		
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
	 * compact-JSON-String to writer
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void toJson(Writer writer) throws IOException;
	
	/**
	 * read JSON file and parse to JsonHub
	 * 
	 * @param JSON-file-path
	 * @return JsonHub
	 * @throws IOException
	 * @throws JsonHubParseException
	 */
	public static JsonHub fromFile(Path path) throws IOException {
		
		try (
				BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
				){
			
			return fromJson(br);
		}
	}
	
	/**
	 * write to file
	 * 
	 * @param file-path
	 * @throws IOException
	 */
	default public void writeFile(Path path) throws IOException {
		
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
	default public void writeFile(Path path, OpenOption... options) throws IOException {
		
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
	default public String prettyPrint() {
		return JsonHubPrettyPrinter.getDefaultPrinter().print(this);
	}
	
	/**
	 * 
	 * @param config
	 * @return Pretty-Print-JSON with config format
	 */
	default public String prettyPrint(JsonHubPrettyPrinterConfig config) {
		return new JsonHubPrettyPrinter(config).print(this);
	}
	
	/**
	 * write Pretty-Print-JSON to writer
	 * 
	 * @param writer
	 * @throws IOException
	 */
	default public void prettyPrint(Writer writer) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, writer);
	}
	
	/**
	 * write Pretty-Print-JSON to writer with config format
	 * 
	 * @param writer
	 * @param config
	 * @throws IOException
	 */
	default public void prettyPrint(Writer writer, JsonHubPrettyPrinterConfig config) throws IOException {
		new JsonHubPrettyPrinter(config).print(this, writer);
	}
	
	/**
	 * write Pretty-Print-JSON to File
	 * 
	 * @param path
	 * @throws IOException
	 */
	default public void prettyPrint(Path path) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, path);
	}
	
	/**
	 * write Pretty-Print-JSON to File
	 * 
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	default public void prettyPrint(Path path, OpenOption... options) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, path, options);
	}
	
	/**
	 * write Pretty-Print-JSON to File with config format
	 * 
	 * @param path
	 * @param config
	 * @throws IOException
	 */
	default public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config) throws IOException {
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
	default public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config, OpenOption... options) throws IOException {
		new JsonHubPrettyPrinter(config).print(this, path, options);
	}
	
	
	/**
	 * 
	 * @param pojo
	 * @return
	 * @throws JsonHubParseException
	 */
	public static JsonHub fromPojo(Object pojo) {
		return JsonHubPojoParser.getInstance().fromPojo(pojo);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param classOfT
	 * @return Pojo
	 * @throws JsonHubParseException
	 */
	default public <T> T toPojo(Class<T> classOfT) {
		return JsonHubPojoParser.getInstance().toPojo(this, classOfT);
	}
	
	/**
	 * 
	 * @return UTF-8 encorded bytes
	 */
	public byte[] getBytes();
	
	/**
	 * write UTF-8 encorded bytes to OutputStream
	 * 
	 * @param OutputSteam
	 * @throws IOException
	 */
	public void writeBytes(OutputStream strm) throws IOException;
	
	/**
	 * 
	 * @param bytes
	 * @return JsonHub
	 * @throws JsonHubParseException
	 */
	public static JsonHub fromBytes(byte[] bs) {
		return fromJson(new String(bs, StandardCharsets.UTF_8));
	}
	
	/**
	 * 
	 * @param strm
	 * @return JsonHub
	 * @throws IOException
	 * @throws JsonHubParseException
	 */
	public static JsonHub fromBytes(InputStream strm) throws IOException {
		
		try (
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				){
			
			for ( ;; ) {
				
				int r = strm.read();
				
				if ( r < 0 ) {
					break;
				}
				
				os.write(r);
			}
			
			return fromBytes(os.toByteArray());
		}
	}

}
