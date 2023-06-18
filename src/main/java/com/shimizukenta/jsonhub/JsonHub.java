package com.shimizukenta.jsonhub;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
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

/**
 * This interface is implements of JSON(RFC 8259) converter, parser, builder, prettyPrint.
 * 
 * <ul>
 * <li>To convert from JSON-String to JsonHub instance, {@link #fromJson(CharSequence)} or {@link #fromJson(Reader)}.</li>
 * <li>To convert from JSON-File to JsonHub instance, {@link #fromFile(Path)}.</li>
 * <li>To convert from JSON-bytes to JsonHub instance, {@link #fromBytes(byte[])} or {@link #fromBytes(InputStream)}.</li>
 * <li>To convert from POJO(Plain-Old-Java-Object) to JsonHub instance, {@link #fromPojo(Object)}.</li>
 * <li>To get JSON-String from JsonHub instance, {@link #toJson()}.</li>
 * <li>To write JSON-File from JsonHub instance, {@link #writeFile(Path)} or {@link #writeFile(Path, OpenOption...)}.</li>
 * <li>To get JSON-String-UTF8-bytes from JsonHub instance, {@link #getBytes()}.</li>
 * <li>To convert from JsonHub instance to POJO, {@link #toPojo(Class)}.</li>
 * </ul>
 * <p>
 * To parse JsonHub,
 * </p>
 * <ul>
 * <li>Methods for seek value in OBJECT or ARRAY,
 * <ul>
 * <li>{@link #get(CharSequence)}</li>
 * <li>{@link #get(String...)}</li>
 * <li>{@link #get(int)}</li>
 * <li>{@link #iterator()}</li>
 * <li>{@link #stream()}</li>
 * <li>{@link #forEach(Consumer)}</li>
 * <li>{@link #forEach(BiConsumer)}</li>
 * <li>{@link #values()}</li>
 * <li>{@link #keySet()}</li>
 * <li>{@link #containsKey(CharSequence)}</li>
 * <li>{@link #getOrDefault(CharSequence)}</li>
 * <li>{@link #getOrDefault(CharSequence, JsonHub)}</li>
 * </ul>
 * </li>
 * <li>Methods for get value,
 * <ul>
 * <li>{@link #intValue()}</li>
 * <li>{@link #longValue()}</li>
 * <li>{@link #doubleValue()}</li>
 * <li>{@link #booleanValue()}</li>
 * <li>{@link #optionalInt()}</li>
 * <li>{@link #optionalLong()}</li>
 * <li>{@link #optionalDouble()}</li>
 * <li>{@link #optionalNubmer()}</li>
 * <li>{@link #optionalBoolean()}</li>
 * <li>{@link #optionalString()}</li>
 * <li>{@link #length()}</li>
 * <li>{@link #isEmpty()}</li>
 * </ul>
 * </li>
 * <li>Methods for check value,
 * <ul>
 * <li>{@link #type()}</li>
 * <li>{@link #isNull()}</li>
 * <li>{@link #nonNull()}</li>
 * <li>{@link #isTrue()}</li>
 * <li>{@link #isFalse()}</li>
 * <li>{@link #isNumber()}</li>
 * <li>{@link #isString()}</li>
 * <li>{@link #isArray()}</li>
 * <li>{@link #isObject()}</li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * To build JsonHub instance, {@link #getBuilder()} and build.<br />
 * </p>
 * <p>
 * To get Pretty-Printing JSON-String, {@link #prettyPrint()}, {@link #prettyPrint(JsonHubPrettyPrinterConfig)}.<br />
 * To write Pretty-Print JSON-File, {@link #prettyPrint(Path)}, {@link #prettyPrint(Path, OpenOption...)},
 * {@link #prettyPrint(Path, JsonHubPrettyPrinterConfig)}, {@link #prettyPrint(Path, JsonHubPrettyPrinterConfig, OpenOption...)}.<br />
 * </p>
 * <p>
 * To get compact JSON-String, {@link #toJson()}.<br />
 * To get compact and exclude null-value-pair in Object JSON-String, {@link #toJsonExcludedNullValueInObject()}.<br />
 * </p>
 * 
 * @author kenta-shimizu
 *
 */
public interface JsonHub extends Iterable<JsonHub> {
	
	/**
	 * Returns iterator if type is OBJECT or ARRAY.
	 * 
	 * <p>
	 * Available if type is ARRAY or OBJECT.<br />
	 * If type is OBJECT, values is JsonHub.<br />
	 * </p>
	 * 
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> (OBJECT or ARRAY)
	 */
	@Override
	public Iterator<JsonHub> iterator();
	
	/**
	 * Returns spliterator if type is OBJECT or ARRAY.
	 * 
	 * <p>
	 * Available if type is ARRAY or OBJECT.<br />
	 * If type is OBJECT, value is JsonHub.<br />
	 * </p>
	 * 
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> (OBJECT or ARRAY)
	 */
	@Override
	public Spliterator<JsonHub> spliterator();
	
	/**
	 * forEach operation if type is OBJECT or ARRAY.
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<br />
	 * If type is OBJECT, value is JsonHub.<br />
	 * </p>
	 * 
	 * @param action Consumer
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> (OBJECT or ARRAY)
	 */
	@Override
	public void forEach(Consumer<? super JsonHub> action);
	
	/**
	 * forEach operation if type is OBJECT or ARRAY.
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<br />
	 * If type is ARRAY, NAME is null.<br />
	 * </p>
	 * 
	 * @param action the BiConsumer
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> (OBJECT or ARRAY)
	 */
	public void forEach(BiConsumer<? super JsonString, ? super JsonHub> action);
	
	
	/**
	 * Returns type, type is NULL, TRUE, FALSE, NUMBER, STRING, ARRAY or OBJECT.
	 * 
	 * @return JsonHubType
	 */
	public JsonHubType type();
	
	/**
	 * Returns java.util.stream.Stream if type is OBJECT or ARRAY.
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<br />
	 * If type is OBJECT, value is JsonHub.<br />
	 * </p>
	 * 
	 * @return Array values stream
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> (OBJECT or ARRAY)
	 */
	public Stream<JsonHub> stream();
	
	/**
	 * Returns set of Object names.
	 * 
	 * <p>
	 * Available if type is OBJECT.<br />
	 * </p>
	 * 
	 * @return set of Object names
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> OBJECT
	 */
	public Set<JsonString> keySet();
	
	/**
	 * Returns list of values.
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<br />
	 * </p>
	 * 
	 * @return list of values
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> (OBJECT or ARRAY)
	 */
	public List<JsonHub> values();
	
	/**
	 * Returns value in Array by index.
	 * 
	 * <p>
	 * Available if type is ARRAY.<br />
	 * </p>
	 *  
	 * @param index index of Array
	 * @return value
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> ARRAY
	 */
	public JsonHub get(int index);
	
	/**
	 * Returns true if contains name in Object.
	 * 
	 * <p>
	 * Available if type is OBJECT.<br />
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param name name of Object
	 * @return true if contains name in Object
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> OBJECT
	 */
	public boolean containsKey(CharSequence name);
	
	/**
	 * Returns JsonHub OBJECT-value instance if Object has same-name, and null otherwise.
	 * 
	 * <p>
	 * Available if type is OBJECT.<br />
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param name name of Object
	 * @return JsonHub value. null if has <i>no</i> same name.
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> OBJECT
	 */
	public JsonHub get(CharSequence name);
	
	/**
	 * Returns JsonHub instance, seek in Object name, if not exist, return empty-ObjectJsonHub.
	 * 
	 * <p>
	 * Available if type is OBJECT
	 * </p>
	 * 
	 * @param name name of Object
	 * @return value, Empty-ObjectJsonHub instance if not exist
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> OBJECT
	 */
	public JsonHub getOrDefault(CharSequence name);
	
	/**
	 * Returns JsonHub instance, seek in Object name, if not exist, return {@code defaultValue}.
	 * 
	 * <p>
	 * Available if type is OBJECT
	 * </p>
	 * 
	 * @param name name of Object
	 * @param defaultValue return this value if not exist.
	 * @return value, defaultValue if <i>not</i> exist
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> OBJECT
	 */
	public JsonHub getOrDefault(CharSequence name, JsonHub defaultValue);
	
	/**
	 * Returns JsonHub instance, seek chains in Object by names, If seek failed, return {@code null}.
	 * 
	 * <p>
	 * Available if type is OBJECT-chains
	 * </p>
	 * 
	 * @param names names of Object
	 * @return value if exist and null otherwise
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> OBJECT
	 */
	public JsonHub get(String... names);
	
	/**
	 * Returns length if type is STRING or OBJECT or ARRAY.
	 * 
	 * <p>
	 * Available if type is STRING or ARRAY or OBJECT
	 * </p>
	 * <p>
	 * If type is STRING return length of String.<br />
	 * If type is ARRAY, return size of values.<br />
	 * If type is OBJECT, return size of name-value-pairs.<br />
	 * </p>
	 * 
	 * @return length
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> (STRING or ARRAY or OBJECT)
	 */
	public int length();
	
	/**
	 * Returns {@code true} if empty.
	 * 
	 * <p>
	 * Available if type is STRING or ARRAY or OBJECT.<br />
	 * </p>
	 * <p>
	 * If type is STRING, return {@code true} if length is 0.<br />
	 * If type is ARRAY, return {@code true} if Array is empty.<br />
	 * If type is OBJECT, return {@code true} if Object-pairs is empty.<Br />
	 * </p>
	 * 
	 * @return {@code true} if empty
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> (STRING or ARRAY or OBJECT)
	 */
	public boolean isEmpty();
	
	/**
	 * Returns {@code true} if type is NULL.
	 * 
	 * @return {@code true} if type is NULL
	 */
	default public boolean isNull() {
		return this.type() == JsonHubType.NULL;
	}
	
	/**
	 * Returns {@code true} if type is not null.
	 * 
	 * @return {@code true} if type is <i>not</i> NULL
	 */
	default public boolean nonNull() {
		return ! this.isNull();
	}
	
	/**
	 * Returns {@code true} if type is TRUE.
	 * 
	 * @return {@code true} if type is TRUE
	 */
	default public boolean isTrue() {
		return this.type() == JsonHubType.TRUE;
	}
	
	/**
	 * Returns {@code true} if type is FALSE.
	 * 
	 * @return {@code true} if type is FALSE
	 */
	default public boolean isFalse() {
		return this.type() == JsonHubType.FALSE;
	}
	
	/**
	 * Returns {@code true} if type is TRUE or FALSE.
	 * 
	 * @return {@code true} if type is TRUE or FALSE
	 */
	default public boolean isBoolean() {
		return this.isTrue() || this.isFalse();
	}
	 
	/**
	 * Returns {@code true} if type is STRING.
	 * 
	 * @return {@code true} is type is STRING
	 */
	default public boolean isString() {
		return this.type() == JsonHubType.STRING;
	}
	
	/**
	 * Returns {@code true} if type is NUMBER.
	 * 
	 * @return {@code true} if type is NUMBER
	 */
	default public boolean isNumber() {
		return this.type() == JsonHubType.NUMBER;
	}
	
	/**
	 * Returns {@code true} if type is ARRAY.
	 * 
	 * @return {@code true} if type is ARRAY
	 */
	default public boolean isArray() {
		return this.type() == JsonHubType.ARRAY;
	}
	
	/**
	 * Returns {@code true} if type is OBJECT.
	 * 
	 * @return {@code true} if type is OBJECT
	 */
	default public boolean isObject() {
		return this.type() == JsonHubType.OBJECT;
	}
	
	/**
	 * Returns Optional, Optional has value if type is TRUE or FALSE, and {@code Optional.empty()} otherwise.
	 * 
	 * @return Optional has value if type is TRUE or FALSE, and {@code Optional.empty()} otherwise
	 */
	public Optional<Boolean> optionalBoolean();
	
	/**
	 * Returns OptionalInt, OptionalInt has value if type is NUMBER, and {@code OptionalInt.empty()} otherwise.
	 * 
	 * @return OptionalInt has value if type is NUMBER, and {@code OptionalInt.empty()} otherwise
	 */
	public OptionalInt optionalInt();
	
	/**
	 * Returns OptionalLong, OptionalLong has value if type is NUMBER, and {@code OptionalLong.empty()} otherwise.
	 * 
	 * @return OptionalLong has value if type is NUMBER, and {@code OptionalLong.empty()} otherwise
	 */
	public OptionalLong optionalLong();
	
	/**
	 * Returns OptionalDouble, OptionalDouble has value if type is NUMBER, and {@code OptionalDouble.empty()} otherwise.
	 * 
	 * @return OptionalDouble has value if type is NUMBER, and {@code OptionalDouble.empty()} otherwise
	 */
	public OptionalDouble optionalDouble();
	
	/**
	 * Returns Optional, Optional has value if type is STRING, and {@code Optional.empty()} otherwise.
	 * 
	 * @return Optional has value if type is STRING, and {@code Optional.empty()} otherwise
	 */
	public Optional<String> optionalString();
	
	/**
	 * Returns Optional, Optional has value if type is NUMBER, and {@code Optional.empty()} otherwise.
	 * 
	 * @return Optional has value if type is NUMBER, and {@code Optional.empty()} otherwise
	 */
	public Optional<Number> optionalNubmer();
	
	/**
	 * Returns boolean value if type is TRUE or FALSE.
	 * 
	 * <p>
	 * Available if type is TRUE or FALSE<br />
	 * </p>
	 * 
	 * @return booleanValue
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> (TRUE or FALSE)
	 */
	default public boolean booleanValue() {
		return optionalBoolean().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #booleanValue"));
	}
	
	/**
	 * Returns int value if type is NUMBER.
	 * 
	 * <p>
	 * Available if type is NUMBER.<br />
	 * </p>
	 * 
	 * @return intValue
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> NUMBER
	 */
	default public int intValue() {
		return optionalInt().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #intValue"));
	}
	
	/**
	 * Returns long value if type is NUMBER.
	 * 
	 * <p>
	 * Available if type is NUMBER.<br />
	 * </p>
	 * 
	 * @return longValue
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> NUMBER
	 */
	default public long longValue() {
		return optionalLong().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #longValue"));
	}
	
	/**
	 * Returns double value if type is NUMBER.
	 * 
	 * <p>
	 * Available if type is NUMBER.<br />
	 * </p>
	 * 
	 * @return doubleValue
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> NUMBER
	 */
	default public double doubleValue() {
		return optionalDouble().orElseThrow(() -> new JsonHubUnsupportedOperationException(type() + " not support #doubleValue"));
	}
	
	/**
	 * Returns JsonHubBuilder.
	 * 
	 * @return JsonHubBuilder instance
	 */
	public static JsonHubBuilder getBuilder() {
		return JsonHubBuilder.getInstance();
	}
	
	/**
	 * Returns JsonHub instance parsing from JSON-String.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param json JSON-string
	 * @return parsed JsonHub instance
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromJson(CharSequence json) {
		return JsonReader.fromJson(json);
	}
	
	/**
	 * Returns parsed JaonHub from Reader.
	 * 
	 * @param reader the reader
	 * @return parsed JsonHub instance
	 * @throws IOException if IO failed
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromJson(Reader reader) throws IOException {
		return JsonReader.fromJson(reader);
	}
	
	/**
	 * Returns parsed compact-JSON-String
	 * 
	 * @return json
	 */
	public String toJson();
	
	/**
	 * Write compact-JSON-String to Writer
	 * 
	 * @param writer writer of JSON-string
	 * @throws IOException if IO failed
	 */
	public void toJson(Writer writer) throws IOException;
	
	/**
	 * Returns parsed compact-JSON-String exclude null value pair in Object;
	 * 
	 * @return json of excluded null value pair in Object.
	 */
	public String toJsonExcludedNullValueInObject();
	
	/**
	 * Returns parsed compact-JSON-String exclude null value pair in Object;
	 * 
	 * @param writer the writer
	 * @throws IOException if IO failed
	 */
	public void toJsonExcludedNullValueInObject(Writer writer) throws IOException;
	
	/**
	 * Returns parsed JsonHub instance from read file.
	 * 
	 * @param path of JSON file
	 * @return parsed JsonHub instance
	 * @throws IOException if IO failed
	 * @throws JsonHubParseException if JsonHub parse failed.
	 */
	public static JsonHub fromFile(Path path) throws IOException {
		return JsonReader.fromFile(path);
	}
	
	/**
	 * Write to file.
	 * 
	 * @param path File-path
	 * @throws IOException if IO failed
	 */
	default public void writeFile(Path path) throws IOException {
		try (
				BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
				) {
			
			toJson(bw);
		}
	}
	
	/**
	 * Write to file with options.
	 * 
	 * @param path File-path
	 * @param options file open options.
	 * @throws IOException if IO failed
	 */
	default public void writeFile(Path path, OpenOption... options) throws IOException {
		try (
				BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, options);
				) {
			
			toJson(bw);
		}
	}
	
	/**
	 * Returns default format Pretty-Print-JSON.
	 * 
	 * @return default format Pretty-Print-JSON
	 */
	default public String prettyPrint() {
		return JsonHubPrettyPrinter.getDefaultPrinter().print(this);
	}
	
	/**
	 * Returns Pretty-Print-JSON with config format.
	 * 
	 * @param config config of Pretty-Print-Format
	 * @return Pretty-Print-JSON with config format
	 */
	default public String prettyPrint(JsonHubPrettyPrinterConfig config) {
		return JsonHubPrettyPrinter.newPrinter(config).print(this);
	}
	
	/**
	 * Write default format Pretty-Print-JSON to writer
	 * 
	 * @param writer writer of JSON-string
	 * @throws IOException if IO failed.
	 */
	default public void prettyPrint(Writer writer) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, writer);
	}
	
	/**
	 * Write Pretty-Print-JSON to writer with config format
	 * 
	 * @param writer writer of JSON-string
	 * @param config config of Pretty-Print-Format
	 * @throws IOException if IO failed.
	 */
	default public void prettyPrint(Writer writer, JsonHubPrettyPrinterConfig config) throws IOException {
		JsonHubPrettyPrinter.newPrinter(config).print(this, writer);
	}
	
	/**
	 * Write default format Pretty-Print-JSON to File
	 * 
	 * @param path File-Path
	 * @throws IOException if io failed.
	 */
	default public void prettyPrint(Path path) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, path);
	}
	
	/**
	 * Write default format Pretty-Print-JSON to File
	 * 
	 * @param path File-Path
	 * @param options OpenOptions
	 * @throws IOException if IO failed.
	 */
	default public void prettyPrint(Path path, OpenOption... options) throws IOException {
		JsonHubPrettyPrinter.getDefaultPrinter().print(this, path, options);
	}
	
	/**
	 * Write Pretty-Print-JSON to File with config format
	 * 
	 * @param path File-Path
	 * @param config config of Pretty-Print-Format
	 * @throws IOException if IO failed.
	 */
	default public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config) throws IOException {
		JsonHubPrettyPrinter.newPrinter(config).print(this, path);
	}
	
	/**
	 * Write Pretty-Print-JSON to File with config format
	 * 
	 * @param path File-Path
	 * @param config config of Pretty-Print-Format
	 * @param options OpenOptions
	 * @throws IOException if IO failed.
	 */
	default public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config, OpenOption... options) throws IOException {
		JsonHubPrettyPrinter.newPrinter(config).print(this, path, options);
	}
	
	/**
	 * Returns JsonHub instance parsing from POJO (Plain-Old-Java-Object).
	 * 
	 * @param pojo (Plain-Old-Java-Object)
	 * @return parsed JsonHub instance
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromPojo(Object pojo) {
		return JsonHubFromPojoParser.parse(pojo);
	}
	
	/**
	 * Returns parsed instance of ClassOtT.
	 * 
	 * @param <T> Type
	 * @param classOfT Class of Type
	 * @return parsed POJO instance
	 * @throws JsonHubParseException if parse failed
	 */
	default public <T> T toPojo(Class<T> classOfT) {
		return JsonHubToPojoParser.parse(this, classOfT);
	}
	
	/**
	 * Returns UTF-8 encorded bytes.
	 * 
	 * @return UTF-8 encorded bytes
	 */
	public byte[] getBytes();
	
	/**
	 * Write UTF-8 encorded bytes to OutputStream
	 * 
	 * @param strm OutputStream
	 * @throws IOException if IO failed.
	 */
	public void writeBytes(OutputStream strm) throws IOException;
	
	/**
	 * Returns UTF-8 encorded bytes excluded null value in Object.
	 * 
	 * @return UTF-8 encorded bytes excluded null value in Object
	 */
	public byte[] getBytesExcludedNullValueInObject();
	
	/**
	 * Write UTF-8 encorded bytes exclued null value in Object to OutputStream
	 * 
	 * @param strm OutputStream
	 * @throws IOException if IO failed
	 */
	public void writeBytesExcludedNullValueInObject(OutputStream strm) throws IOException;
	
	/**
	 * Returns parsed JsonHub instance from JSON-UTF8-bytes-array.
	 * 
	 * @param bs JSON-UTF8-bytes-array
	 * @return parsed JsonHub instance
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromBytes(byte[] bs) {
		return JsonReader.fromBytes(bs);
	}
	
	/**
	 * Returns parsed JsonHub instance from JSON-UTF8-bytes-stream.
	 * 
	 * @param strm JSON-UTF8-bytes-stream
	 * @return parsed JsonHub instance
	 * @throws IOException if IO failed
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromBytes(InputStream strm) throws IOException {
		return JsonReader.fromBytes(strm);
	}
	
	/**
	 * Parse with JsonPath.
	 * 
	 * @param jsonPath the JsonPath
	 * @return List of JsonHub
	 */
	default public List<JsonHub> jsonPath(CharSequence jsonPath) {
		return JsonPathParser.parse(this, jsonPath);
	}
	
}
