package com.shimizukenta.jsonhub;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
 * <p>
 * To convert from JSON-String to JsonHub instance, {@link #fromJson(CharSequence)} or {@link #fromJson(Reader)}.<br />
 * To convert from JSON-File to JsonHub instance, {@link #fromFile(Path)}.<br />
 * To convert from JSON-bytes to JsonHub instance, {@link #fromBytes(byte[])} or {@link #fromBytes(InputStream)}.<br />
 * To convert from POJO(Plain-Old-Java-Object) to JsonHub instance, {@link #fromPojo(Object)}.<brr />
 * 
 * </p>
 * <p>
 * To parse JsonHub
 * </p>
 * <p>
 * To build JsonHub instance, {@link #getBuilder()} and build.<br />
 * </p>
 * <p>
 * To prettyPrint
 * </p>
 * 
 * @author kenta-shimizu
 *
 */
public interface JsonHub extends Iterable<JsonHub> {
	
	/**
	 * Returns iterator
	 * 
	 * <p>
	 * Available if type is ARRAY or OBJECT.<br />
	 * If type is OBJECT, values is JsonHub.<br />
	 * </p>
	 * 
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT or ARRAY
	 */
	@Override
	public Iterator<JsonHub> iterator();
	
	/**
	 * Returns spliterator.
	 * 
	 * <p>
	 * Available if type is ARRAY or OBJECT.<br />
	 * If type is OBJECT, value is JsonHub.<br />
	 * </p>
	 * 
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT or ARRAY
	 */
	@Override
	public Spliterator<JsonHub> spliterator();
	
	/**
	 * forEach operation.
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<br />
	 * If type is OBJECT, value is JsonHub.<bt />
	 * </p>
	 * 
	 * @param action Consumer<JsonHub>
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT or ARRAY
	 */
	@Override
	public void forEach(Consumer<? super JsonHub> action);
	
	/**
	 * forEach operation.
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<br />
	 * If type is ARRAY, NAME is null.<br />
	 * </p>
	 * 
	 * @param action BiConsumer<JsonString, JsonHub>
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT or ARRAY
	 */
	public void forEach(BiConsumer<? super JsonString, ? super JsonHub> action);
	
	
	/**
	 * type getter.
	 * 
	 * @return JsonHubType
	 */
	public JsonHubType type();
	
	/**
	 * java.util.stream.Stream getter
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<br />
	 * If type is OBJECT, value is JsonHub.<br />
	 * </p>
	 * 
	 * @return Array values stream
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT or ARRAY
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
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT
	 */
	public Set<JsonString> keySet();
	
	/**
	 * Returns list of values.
	 * 
	 * <p>
	 * Available if type is OBJECT or ARRAY.<bt />
	 * </p>
	 * 
	 * @return list of values
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT or ARRAY
	 */
	public List<JsonHub> values();
	
	/**
	 * Retuns vslue in Array by index.
	 * 
	 * <p>
	 * Available if type is ARRAY.<br />
	 * </p>
	 *  
	 * @param index
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(int index);
	
	/**
	 * Returns true if contains name in Object.
	 * 
	 * <p>
	 * Available if type is OBJECT.<br />
	 * </p>
	 * 
	 * @param name
	 * @return true if contains name in Object
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> OBJECT
	 */
	public boolean containsKey(CharSequence name);
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return value. null if not has name.
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub get(CharSequence name);
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @return emptyObject() if not exist
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub getOrDefault(CharSequence name);
	
	/**
	 * available if type is OBJECT
	 * 
	 * @param name
	 * @param defaultValue
	 * @return defaultValue if not exist
	 * @throws JsonHubUnsupportedOperationException
	 */
	public JsonHub getOrDefault(CharSequence name, JsonHub defaultValue);
	
	/**
	 * available if type is OBJECT-chains
	 * 
	 * @param names
	 * @return value
	 * @throws JsonHubUnsupportedOperationException if parse failed
	 */
	public JsonHub get(String... names);
	
	/**
	 * Returns length.
	 * 
	 * <p>
	 * Available if STRING or ARRAY or OBJECT
	 * </p>
	 * <p>
	 * If type is STRING return length of String.<br />
	 * If type is ARRAY, return size of values.<br />
	 * If type is OBJECT, return size of object-pairs.<br />
	 * </p>
	 * 
	 * @return length
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> STRING or ARRAY or OBJECT
	 */
	public int length();
	
	/**
	 * Returns {@code true} if empty.
	 * 
	 * <p>
	 * Available if STRING or ARRAY or OBJECT.<br />
	 * <p>
	 * <p>
	 * If type is STRING, return {@code true} if length is 0.<br />
	 * If type is ARRAY, return {@code true} if Array is empty.<br />
	 * If type is OBJECT, return {@code true} if Object-pairs is empty.<Br />
	 * </p>
	 * 
	 * @return {@code true} if empty
	 * @throws JsonHubUnsupportedOperationException if <i>not</i> STRING or ARRAY or OBJECT
	 */
	public boolean isEmpty();
	
	/**
	 * Returns {@code true} if type is NULL.
	 * 
	 * @return {@code true} if type is NULL
	 */
	public boolean isNull();
	
	/**
	 * Returns {@code true} if type is not null.
	 * 
	 * @return {@code true} if type is <i>not</i> NULL
	 */
	public boolean nonNull();
	
	/**
	 * Returns {@code true} if type is TRUE.
	 * 
	 * @return {@code true} if type is TRUE
	 */
	public boolean isTrue();
	
	/**
	 * Returns {@code true} if type is FALSE.
	 * 
	 * @return {@code true} if type is FALSE
	 */
	public boolean isFalse();
	
	/**
	 * Returns {@code true} if type is STRING.
	 * 
	 * @return {@code true} is type is STRING
	 */
	public boolean isString();
	
	/**
	 * Returns {@code true} if  type is NUMBER.
	 * 
	 * @return {@code true} if type is NUMBER
	 */
	public boolean isNumber();
	
	/**
	 * Returns {@code true} if type is ARRAY.
	 * 
	 * @return {@code true} if type is ARRAY
	 */
	public boolean isArray();
	
	/**
	 * Returns {@code true} if type is OBJECT.
	 * 
	 * @return {@code true} if type is OBJECT
	 */
	public boolean isObject();
	
	/**
	 * Returns Optional, Optional has value if type is TRUE or FALSE, otherwise {@code Optional.empty()}.
	 * 
	 * @return Optional has value if type is TRUE or FALSE, oherwise {@code Optional.empty()}
	 */
	public Optional<Boolean> optionalBoolean();
	
	/**
	 * Returns OptionalInt, OptionalInt has value if type is NUMBER, oherwise {@code OptionalInt.empty()}.
	 * 
	 * @return Optional has value if type is NUMBER, oherwise {@code OptionalInt.empty()}
	 */
	public OptionalInt optionalInt();
	
	/**
	 * 
	 * @return Optional has value if type is NUMBER
	 */
	public OptionalLong optionalLong();
	
	/**
	 * 
	 * @return Optional has value if type is NUMBER
	 */
	public OptionalDouble optionalDouble();
	
	/**
	 * String type value getter.
	 * 
	 * @return Optional has value if type is STRING
	 */
	public Optional<String> optionalString();
	
	/**
	 * 
	 * @return Optional has value if type is NUMBER
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
	 * @throws JsonHubUnsupportedOperationException if type is <i>not</i> TRUE or FALSE
	 */
	public boolean booleanValue();
	
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
	public int intValue();
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public long longValue();
	
	/**
	 * available if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonHubUnsupportedOperationException
	 */
	public double doubleValue();
	
	
	/**
	 * JsonHubBuilder getter.
	 * 
	 * @return JsonHubBuilder instance
	 */
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
	 * Parse to JaonHub
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
	 * Parse to compact-JSON-String
	 * 
	 * @return json
	 */
	public String toJson();
	
	/**
	 * Write compact-JSON-String to writer
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void toJson(Writer writer) throws IOException;
	
	/**
	 * Parse to compact-JSON-String exclued null value in Object;
	 * 
	 * @return json of excluded null value in Object.
	 */
	public String toJsonExcludedNullValueInObject();
	
	/**
	 * Parse to compact-JSON-String exclued null value in Object;
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void toJsonExcludedNullValueInObject(Writer writer) throws IOException;
	
	/**
	 * Read JSON file and parse to JsonHub
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
	 * Write to file
	 * 
	 * @param file-path
	 * @throws IOException
	 */
	public void writeFile(Path path) throws IOException;
	
	/**
	 * Write to file
	 * 
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	public void writeFile(Path path, OpenOption... options) throws IOException;
	
	
	/**
	 * 
	 * @return Pretty-Print-JSON
	 */
	public String prettyPrint();
	
	/**
	 * 
	 * @param config
	 * @return Pretty-Print-JSON with config format
	 */
	public String prettyPrint(JsonHubPrettyPrinterConfig config);
	
	/**
	 * write Pretty-Print-JSON to writer
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void prettyPrint(Writer writer) throws IOException;
	
	/**
	 * write Pretty-Print-JSON to writer with config format
	 * 
	 * @param writer
	 * @param config
	 * @throws IOException
	 */
	public void prettyPrint(Writer writer, JsonHubPrettyPrinterConfig config) throws IOException;
	
	/**
	 * write Pretty-Print-JSON to File
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void prettyPrint(Path path) throws IOException;
	
	/**
	 * write Pretty-Print-JSON to File
	 * 
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	public void prettyPrint(Path path, OpenOption... options) throws IOException;
	
	/**
	 * write Pretty-Print-JSON to File with config format
	 * 
	 * @param path
	 * @param config
	 * @throws IOException
	 */
	public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config) throws IOException;
	
	/**
	 * write Pretty-Print-JSON to File with config format
	 * 
	 * @param path
	 * @param config
	 * @param options
	 * @throws IOException
	 */
	public void prettyPrint(Path path, JsonHubPrettyPrinterConfig config, OpenOption... options) throws IOException;
	
	
	/**
	 * 
	 * @param pojo
	 * @return
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromPojo(Object pojo) {
		return JsonHubFromPojoParser.getInstance().parse(pojo);
	}
	
	/**
	 * 
	 * @param <T>
	 * @param classOfT
	 * @return Pojo
	 * @throws JsonHubParseException
	 */
	public <T> T toPojo(Class<T> classOfT);
	
	/**
	 * 
	 * @return UTF-8 encorded bytes
	 */
	public byte[] getBytes();
	
	/**
	 * Write UTF-8 encorded bytes to OutputStream
	 * 
	 * @param strm
	 * @throws IOException
	 */
	public void writeBytes(OutputStream strm) throws IOException;
	
	/**
	 * 
	 * @return UTF-8 encorded bytes excluded null value in Object.
	 */
	public byte[] getBytesExcludedNullValueInObject();
	
	/**
	 * Write UTF-8 encorded bytes exclued null value in Object to OutputStream
	 * 
	 * @param strm
	 * @throws IOException
	 */
	public void writeBytesExcludedNullValueInObject(OutputStream strm) throws IOException;
	
	/**
	 * Parse from JSON-UTF8-bytes-array to JsonHub instance.
	 * 
	 * @param bs JSON-UTF8-bytes-array
	 * @return JsonHub
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromBytes(byte[] bs) {
		return fromJson(new String(bs, StandardCharsets.UTF_8));
	}
	
	/**
	 * Parse from JSON-UTF8-bytes-stream to JsonHub instance.
	 * 
	 * @param strm JSON-UTF8-bytes-stream
	 * @return JsonHub
	 * @throws IOException
	 * @throws JsonHubParseException if parse failed
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
