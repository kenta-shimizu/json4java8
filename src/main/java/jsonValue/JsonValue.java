package jsonValue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;


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
		throw new JsonValueUnsupportedOperationException();
	}
	
	abstract public JsonValueType type();
	
	/**
	 * enable if type is ARRAY
	 * 
	 * @return Array values stream
	 * @throws JsonValueUnsupportedOperationException
	 */
	public Stream<JsonValue> stream() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @return names
	 * @throws JsonValueUnsupportedOperationException
	 */
	public Set<String> keySet() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * enable if type is OBJECT or ARRAY
	 * 
	 * @return values
	 * @throws JsonValueUnsupportedOperationException
	 */
	public List<JsonValue> values() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param consumer<NAME, VALUE>
	 * @throws JsonValueUnsupportedOperationException
	 */
	public void forEach(BiConsumer<String, JsonValue> consumer) {
		throw new JsonValueUnsupportedOperationException("Type is not Object");
	}
	
	/**
	 * enable if type is ARRAY
	 *  
	 * @param index
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public JsonValue get(int index) {
		throw new JsonValueUnsupportedOperationException("get: " + index);
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @return true if has name
	 * @throws JsonValueUnsupportedOperationException
	 */
	public boolean containsKey(CharSequence name) {
		throw new JsonValueUnsupportedOperationException("containsKey: \"" + name + "\"");
	}
	
	/**
	 * enable if type is OBJECT
	 * 
	 * @param name
	 * @return value. null if not has name.
	 * @throws JsonValueUnsupportedOperationException
	 */
	public JsonValue get(CharSequence name) {
		throw new JsonValueUnsupportedOperationException("get: \"" + name + "\"");
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
	
	protected JsonValue _get(LinkedList<String> ll) {
		
		if ( ll.isEmpty() ) {
			return this;
		} else {
			String s = ll.removeFirst();
			return get(s)._get(ll);
		}
	}
	
	/**
	 * enable if STRING or ARRAY
	 * 
	 * @return length
	 * @throws JsonValueUnsupportedOperationException
	 */
	public int length() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * 
	 * @return true if value is null
	 */
	public boolean isNull() {
		return false;
	}
	
	/**
	 * 
	 * @return true if value is not null
	 */
	public boolean notNull() {
		return ! isNull();
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public boolean booleanValue() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public int intValue() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public long longValue() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public float floatValue() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	/**
	 * enable if type is NUMBER
	 * 
	 * @return value
	 * @throws JsonValueUnsupportedOperationException
	 */
	public double doubleValue() {
		throw new JsonValueUnsupportedOperationException();
	}
	
	
	/* builders */
	
	/**
	 * parse to JsonValue
	 * 
	 * @param json
	 * @return JsonValue
	 */
	public static JsonValue fromJson(CharSequence json) {
		return new JsonUnparsedValue(json);
	}
	
	/**
	 * parse to JaonValue
	 * 
	 * @param reader
	 * @return JsonValue
	 * @throws IOException
	 */
	public static JsonValue fromJson(Reader reader) throws IOException {
		
		try (
				CharArrayWriter writer = new CharArrayWriter();
				) {
			
			try (
					BufferedReader br = new BufferedReader(reader);
					) {
				
				for ( ;; ) {
					
					int r = br.read();
					if ( r < 0 ) {
						break;
					}
					
					writer.write(r);
				}
			}
			
			return fromJson(writer.toString());
		}
	}
	
	
	protected static final byte BACKSLASH = 0x5C;
	protected static final byte UNICODE = 0x75;
	
	protected enum EscapeSets {
		
		BS(0x08, 0x62),	/* b */
		HT(0x09, 0x74),	/* t */
		LF(0x0A, 0x6E),	/* n */
		FF(0x0C, 0x66),	/* f */
		CR(0x0D, 0x72),	/* r */
		QUOT(0x22, 0x22),	/* " */
		SLASH(0x2F, 0x2F),	/* / */
		BSLASH(BACKSLASH, BACKSLASH), /* \ */
		;
		
		private byte a;
		private byte b;
		
		private EscapeSets(int a, int b) {
			this.a = (byte)a;
			this.b = (byte)b;
		}
		
		public static Byte escape(byte b) {
			
			for ( EscapeSets x : values() ) {
				if ( x.a == b ) {
					return Byte.valueOf(x.b);
				}
			}
			
			return null;
		}
		
		public static Byte unescape(byte b) {
			
			for ( EscapeSets x : values() ) {
				if ( x.b == b ) {
					return Byte.valueOf(x.a);
				}
			}
			
			return null;
		}
	}
	
	public static String escape(CharSequence cs) {
		
		try (
				ByteArrayOutputStream strm = new ByteArrayOutputStream();
				) {
			
			byte[] bb = cs.toString().getBytes(StandardCharsets.UTF_8);
			
			for (byte b : bb) {
				
				Byte x = EscapeSets.escape(b);
				
				if ( x == null ) {
					
					strm.write(b);
					
				} else {
					
					strm.write(BACKSLASH);
					strm.write(x.byteValue());
				}
			}
			
			return new String(strm.toByteArray(), StandardCharsets.UTF_8);
		}
		catch ( IOException notHappen ) {
			throw new RuntimeException(notHappen);
		}
	}
	
	
	/**
	 * parse to minimun Json String
	 * 
	 * @return json
	 */
	abstract public String toJson();
	
	private static final JsonTrueValue _trueValue = new JsonTrueValue();
	public static JsonTrueValue trueValue() {
		return _trueValue;
	}
	
	private static final JsonFalseValue _falseValue = new JsonFalseValue();
	public static JsonFalseValue falseValue() {
		return _falseValue;
	}
	
	private static final JsonNullValue _nullValue = new JsonNullValue();
	public static JsonNullValue nullValue() {
		return _nullValue;
	}
	
}
