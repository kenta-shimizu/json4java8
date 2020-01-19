package jsonValue;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class JsonValueParser {

	private JsonValueParser() {
		/* Nothing */
	}
	
	private static class SingletonHolder {
		private static final JsonValueParser inst = new JsonValueParser();
	}
	
	public static JsonValueParser getInstance() {
		return SingletonHolder.inst;
	}
	
	/**
	 * 
	 * @param cs
	 * @return
	 * @thows JsonValueParseException
	 */
	public JsonValue parse(CharSequence cs) {
		
		try {
			return _fromJson(cs.toString());
		}
		catch ( JsonValueIndexOutOfBoundsException | JsonValueNumberFormatException e ) {
			throw new JsonValueParseException(e);
		}
	}
	
	/**
	 * 
	 * @param reader
	 * @return jsonValue
	 * @throws JsonValueParseException
	 * @throws IOException
	 */
	public JsonValue parse(Reader reader) throws IOException {
		
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
			
			return parse(writer.toString());
		}
	}
	
	
	private static final String L_NULL = "null";
	private static final String L_TRUE = "true";
	private static final String L_FALSE = "false";
	private static final String V_QUOT = "\"";
	private static final String V_OLBK = "{";
	private static final String V_ORBK = "}";
	private static final String V_ALBK = "[";
	private static final String V_ARBK = "]";
	
	private static JsonValue _fromJson(String v) {
		
		String s = v.trim();
		
		if ( s.equals(L_NULL) ) {
			
			return JsonValueBuilder.getInstance().nullValue();
			
		} else if ( s.equals(L_TRUE) ) {
			
			return JsonValueBuilder.getInstance().trueValue();
			
		} else if ( s.equals(L_FALSE) ) {
			
			return JsonValueBuilder.getInstance().falseValue();
			
		} else if ( s.startsWith(V_QUOT) && s.endsWith(V_QUOT) ) {
			
			return _fromJsonStringValue(s);
			
		} else if ( s.startsWith(V_OLBK) && s.endsWith(V_ORBK) ) {
			
			return _fromJsonObjectValue(s);
			
		} else if ( s.startsWith(V_ALBK) && s.endsWith(V_ARBK) ) {
			
			return _fromJsonArrayValue(s);
			
		} else {
			
			return _fromJsonNumberValue(s);
		}
	}
	
	private static JsonValue _fromJsonStringValue(String s) {
		return JsonValueBuilder.getInstance().build(JsonString.escaped(peelString(s)));
	}
	
	private static JsonNumberValue _fromJsonNumberValue(String s) {
		return new JsonNumberValue(s);
	}
	
	private static JsonValue _fromJsonArrayValue(String s) {
		
		final List<JsonValue> ll = new ArrayList<>();
		
		String v = peelString(s).trim();
		
		for ( int i = 0, len = v.length(); i < len; ) {
			
			
			//TODO
		}
		
		return JsonValueBuilder.getInstance().build(ll);
	}
	
	private static JsonValue _fromJsonObjectValue(String s) {
		
		final List<JsonObjectPair> pairs = new ArrayList<>();
		
		String v = peelString(s).trim();
		
		for ( int i = 0, len = v.length(); i < len; ) {
			
			
			//TODO
			
			//getObjectName
			//seek coron
			//detect next object
			
		}
		
		return JsonValueBuilder.getInstance().build(pairs);
	}
	
	private static String peelString(String v) {
		return v.substring(1, (v.length() - 1));
	}
	
	private static int seekIndexOfComma(String s, int startIndex) {
		
		//TODO
		
		return -1;
	}
	
	private static int seekIndexOfColon(String s, int startIndex) {
		
		//TODO
		
		return -1;
	}
	
	private static int seekEndIndexOfString(String s, int startIndex) {
		
		//TODO
		
		return -1;
	}
	
	private static int seekEndIndexOfArray(String s, int startIndex) {
		
		//TODO
		
		return -1;
	}
	
	private static int seekEndIndexOfObject(String s, int startIndex) {
		
		//TODO
		
		return -1;
	}
	
	private static class SeekResult {
		
		private final String value;
		private final int index;
		
		private SeekResult(String s, int index) {
			this.value = s;
			this.index = index;
		}
	}
	
	private static SeekResult seekNext(String s, int startIndex) {
		
		//TODO
		
		return new SeekResult("", -1);
	}
	
}
