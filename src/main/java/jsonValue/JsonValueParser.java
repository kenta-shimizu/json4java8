package jsonValue;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
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
			return fromJson(cs.toString());
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
	
	private static final String S_BSLASH = "\\";
	private static final String S_QUOT = "\"";
	private static final String S_OLBK = "{";
	private static final String S_ORBK = "}";
	private static final String S_ALBK = "[";
	private static final String S_ARBK = "]";
	private static final String S_COMMA = ",";
	private static final String S_COLON = ":";
	
	private static final char C_WS_MAX = 0x0020;
	private static final char C_BSLASH = S_BSLASH.charAt(0);
	private static final char C_QUOT = S_QUOT.charAt(0);
	private static final char C_OLBK = S_OLBK.charAt(0);
	private static final char C_ORBK = S_ORBK.charAt(0);
	private static final char C_ALBK = S_ALBK.charAt(0);
	private static final char C_ARBK = S_ARBK.charAt(0);
	private static final char C_COMMA = S_COMMA.charAt(0);
	private static final char C_COLON = S_COLON.charAt(0);
	
	
	private static class SeekCharResult {
		
		private final char c;
		private final int index;
		
		private SeekCharResult(char c, int index) {
			this.c = c;
			this.index = index;
		}
	}
	
	private static class SeekValueResult {
		
		private final JsonValue value;
		private final int endIndex;
		
		private SeekValueResult(JsonValue v, int index) {
			this.value = v;
			this.endIndex = index;
		}
	}
	
	
	private static JsonValue fromJson(String str) {
		
		SeekCharResult r = seekNextChar(str, 0);
		
		SeekValueResult vr;
		
		if (r.c == C_QUOT) {
			
			vr = fromJsonStringValue(str, r.index);
			
			
		} else if (r.c == C_ALBK) {
			
			vr = fromJsonArrayValue(str, r.index);
			
		} else if (r.c == C_OLBK) {
			
			vr = fromJsonObjectValue(str, r.index);
			
		} else {
			
			vr = fromJsonNotStructuralValue(str, r.index);
			
			if ( vr.endIndex < 0 ) {
				
				return vr.value;
				
			} else {
				
				throw new JsonValueParseException("Value is not Single \"" + str + "\"");
			}
		}
		
		if ( seekNextChar(str, vr.endIndex).index < 0 ) {
			
			return vr.value;
			
		} else {
			
			throw new JsonValueParseException("Value is not Single \"" + str + "\"");
		}
	}
	
	private static SeekValueResult fromJsonNotStructuralValue(String str, int fromIndex) {
		
		SeekCharResult r = seekNextEndDelimiter(str, fromIndex);
		
		String s;
		
		if ( r.index < 0 ) {
			
			s = str.substring(fromIndex).trim();
			
		} else {
			
			s = str.substring(fromIndex, r.index).trim();
		}
		
		if ( s.equals(L_NULL) ) {
			
			return new SeekValueResult(
					JsonValueBuilder.getInstance().nullValue()
					, r.index);
			
		} else if ( s.equals(L_TRUE) ) {
			
			return new SeekValueResult(
					JsonValueBuilder.getInstance().trueValue()
					, r.index);
			
		} else if ( s.equals(L_FALSE) ) {
			
			return new SeekValueResult(
					JsonValueBuilder.getInstance().falseValue()
					, r.index);
			
		} else {
			
			return new SeekValueResult(
					fromJsonNumberValue(s)
					, r.index);
		}
	}
	
	private static JsonString fromJsonString(String str) {
		String s = str.trim();
		return JsonString.escaped(s.substring(1, (s.length() - 1)));
	}
	
	private static SeekValueResult fromJsonStringValue(String str, int fromIndex) {
		
		int endIndex = seekEndIndexOfString(str, fromIndex);
		
		return new SeekValueResult(
				JsonValueBuilder.getInstance().string(
						fromJsonString(str.substring(fromIndex, endIndex)))
				, endIndex);
	}
	
	private static SeekValueResult fromJsonArrayValue(String str, int fromIndex) {
		
		final List<JsonValue> ll = new ArrayList<>();
		
		for (int i = (fromIndex + 1), len = str.length(); i < len;) {
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( r.c == C_ARBK ) {
					
					return new SeekValueResult(
							JsonValueBuilder.getInstance().array(ll)
							, r.index + 1);
					
				} else if ( r.c == C_QUOT ) {
					
					SeekValueResult vr = fromJsonStringValue(str, r.index);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if ( r.c == C_ALBK ) {
					
					SeekValueResult vr = fromJsonArrayValue(str, r.index);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if ( r.c == C_OLBK ) {
					
					SeekValueResult vr = fromJsonObjectValue(str, r.index);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if ((r.c == C_COLON) || (r.c == C_COMMA)) {
					
					throw new JsonValueParseException("Value is empty \"" + str + "\"");
					
				} else {
					
					SeekValueResult vr = fromJsonNotStructuralValue(str, r.index);
					
					if ( vr.endIndex < 0 ) {
						
						throw new JsonValueParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
						
					} else {
						
						ll.add(vr.value);
						i = vr.endIndex;
					}
				}
			}
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( r.c == C_COMMA ) {
					
					i = r.index + 1;
					
				} else if ( r.c == C_ARBK ) {
					
					i = r.index;
					
				} else {
					
					throw new JsonValueParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
				}
			}
		}
		
		throw new JsonValueParseException("Not found end-of-ARRAY. fromIndex: " + fromIndex + " \"" + str + "\"");
	}
	
	private static SeekValueResult fromJsonObjectValue(String str, int fromIndex) {
		
		final JsonValueBuilder jvb = JsonValueBuilder.getInstance();
		
		final Collection<JsonObjectPair> pairs = new ArrayList<>();
		
		for (int i = (fromIndex + 1), len = str.length(); i < len;) {
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( r.c == C_ORBK ) {
					
					return new SeekValueResult(
							jvb.object(pairs)
							, r.index + 1);
				}
				
				i = r.index;
			}
			
			int nameStartIndex = seekIndexOfNextQuot(str, i);
			int nameEndIndex = seekEndIndexOfString(str, nameStartIndex);
			
			JsonString js = fromJsonString(str.substring(nameStartIndex, nameEndIndex));
			
			i = seekIndexOfNextColon(str, nameEndIndex) + 1;
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( r.c == C_QUOT ) {
					
					SeekValueResult vr = fromJsonStringValue(str, r.index);
					pairs.add(jvb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if ( r.c == C_ALBK ) {
					
					SeekValueResult vr = fromJsonArrayValue(str, r.index);
					pairs.add(jvb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if ( r.c == C_OLBK ) {
					
					SeekValueResult vr = fromJsonObjectValue(str, r.index);
					pairs.add(jvb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if ((r.c == C_COLON) || (r.c == C_COMMA)) {
					
					throw new JsonValueParseException("Value is empty. index: " + r.index + " \"" + str + "\"");
					
				} else {
					
					SeekValueResult vr = fromJsonNotStructuralValue(str, r.index);
					
					if ( vr.endIndex < 0 ) {
						
						throw new JsonValueParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
						
					} else {
						
						pairs.add(jvb.pair(js, vr.value));
						i = vr.endIndex;
					}
				}
			}
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( r.c == C_COMMA ) {
					
					i = r.index + 1;
					
				} else if ( r.c == C_ORBK ) {
					
					i = r.index;
					
				} else {
					
					throw new JsonValueParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
				}
			}
		}
		
		throw new JsonValueParseException("Not found end-of-OBJECT. fromIndex: " + fromIndex + " \"" + str + "\"");
	}
	
	private static JsonNumberValue fromJsonNumberValue(String str) {
		return JsonValueBuilder.getInstance().number(str);
	}
	
	private static int seekIndexOfNextQuot(String str, int fromIndex) {
		SeekCharResult r = seekNextChar(str, fromIndex);
		if ((r.index >= 0) && (r.c == C_QUOT)) {
			return r.index;
		} else {
			throw new JsonValueParseException("Not found Quot. fromIndex: " + fromIndex + " \"" + str + "\"");
		}
	}
	
	private static int seekIndexOfNextColon(String str, int fromIndex) {
		SeekCharResult r = seekNextChar(str, fromIndex);
		if ((r.index >= 0) && (r.c == C_COLON)) {
			return r.index;
		} else {
			throw new JsonValueParseException("Not found \":\" fromIndex: " + fromIndex + " \"" + str + "\"");
		}
	}
	
	private static int seekEndIndexOfString(String str, int fromIndex) {
		
		for (int i = (fromIndex + 1), len = str.length(); i < len; ++i) {
			
			char c = str.charAt(i);
			
			if ( c == C_BSLASH ) {
				++i;
				continue;
			}
			
			if ( c == C_QUOT ) {
				return i + 1;
			}
		}
		
		throw new JsonValueParseException("Not found end-of-STRING. fromIndex: " + fromIndex + " \"" + str + "\"");
	}
	
	private static SeekCharResult seekNextChar(String str, int fromIndex) {
		
		for (int i = fromIndex, len = str.length(); i < len; ++i) {
			
			char c = str.charAt(i);
			
			if ( c > C_WS_MAX ) {
				return new SeekCharResult(c, i);
			}
		}
		
		return new SeekCharResult(C_WS_MAX, -1);
	}
	
	private static final char[] delimiters = new char[] {C_COMMA, C_ARBK, C_ORBK};
	
	private static SeekCharResult seekNextEndDelimiter(String str, int fromIndex) {
		
		for (int i = fromIndex, len = str.length(); i < len; ++i) {
			
			char c = str.charAt(i);
			
			for (char d : delimiters ) {
				
				if ( c == d ) {
					return new SeekCharResult(c, i);
				}
			}
		}
		
		return new SeekCharResult(C_WS_MAX, -1);
	}
	
}
