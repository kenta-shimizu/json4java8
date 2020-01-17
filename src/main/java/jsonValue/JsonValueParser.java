package jsonValue;

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
			return _parse(cs.toString());
		}
		catch ( JsonValueIndexOutOfBoundsException | JsonValueNumberFormatException e ) {
			throw new JsonValueParseException(e);
		}
	}
	
	private JsonValue _parse(String v) {
		
		String s = v.trim();
		
		if ( s.equals("null") ) {
			
			return JsonValue.nullValue();
			
		} else if ( s.equals("true") ) {
			
			return JsonValue.trueValue();
			
		} else if ( s.equals("false") ) {
			
			return JsonValue.falseValue();
			
		} else if ( s.startsWith("\"") && s.endsWith("\"") ) {
			
			return parseString(s);
			
		} else if ( s.startsWith("{") && s.endsWith("}") ) {
			
			return parseObject(s);
			
		} else if ( s.startsWith("[") && s.endsWith("]") ) {
			
			return parseArray(s);
			
		} else {
			
			return parseNumber(s);
		}
	}
	
	private JsonStringValue parseString(String s) {
		return JsonStringValue.unescape(peelString(s));
	}
	
	private JsonObjectValue parseObject(String s) {
		
		//TODO
		
		return null;
	}
	
	private JsonArrayValue parseArray(String s) {
		
		//TODO
		
		return null;
	}
	
	private JsonNumberValue parseNumber(String s) {
		return new JsonNumberValue(s);
	}
	
	private static String peelString(String v) {
		return v.substring(1, (v.length() - 1));
	}
	
}
