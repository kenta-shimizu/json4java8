package com.shimizukenta.jsonhub;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JSON reader to JsonHub.
 * 
 * @author kenta-shimizu
 *
 */
public final class JsonReader {

	private JsonReader() {
	}
	
	/**
	 * Returns JsonHub instance parsing from JSON-String.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param json
	 * @return parsed JsonHub instance
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromJson(CharSequence json) {
		try {
			String s = json.toString();
			
			if ( s.trim().isEmpty() ) {
				throw new JsonHubParseException("JSON is empty");
			}
			
			return parse(s);
		}
		catch ( JsonHubIndexOutOfBoundsException | JsonHubNumberFormatException e ) {
			throw new JsonHubParseException(e);
		}
	}
	
	/**
	 * Returns parsed JaonHub from Reader.
	 * 
	 * @param reader
	 * @return parsed JsonHub instance
	 * @throws IOException
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromJson(Reader reader) throws IOException {
		try (
				CharArrayWriter writer = new CharArrayWriter();
				) {
			
			for ( ;; ) {
				
				int r = reader.read();
				
				if ( r < 0 ) {
					break;
				}
				
				writer.write(r);
			}
			
			return fromJson(writer.toString());
		}
	}
	
	/**
	 * Returns parsed JsonHub instance from read file.
	 * 
	 * @param path of JSON file
	 * @return parsed JsonHub instance
	 * @throws IOException
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromFile(Path path) throws IOException {
		try (
				BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
				) {
			return fromJson(br);
		}
	}
	
	/**
	 * Returns parsed JsonHub instance from JSON-UTF8-bytes-array.
	 * 
	 * @param bs JSON-UTF8-bytes-array
	 * @return parsed JsonHub instance
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromBytes(byte[] bs) {
		return fromJson(new String(bs, StandardCharsets.UTF_8));
	}
	
	/**
	 * Returns parsed JsonHub instance from JSON-UTF8-bytes-stream.
	 * 
	 * @param strm JSON-UTF8-bytes-stream
	 * @return parsed JsonHub instance
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
	
	private static class SeekCharResult {
		
		private final char c;
		private final int index;
		
		private SeekCharResult(char c, int index) {
			this.c = c;
			this.index = index;
		}
	}
	
	private static class SeekValueResult {
		
		private final AbstractJsonHub value;
		private final int endIndex;
		
		private SeekValueResult(AbstractJsonHub v, int index) {
			this.value = v;
			this.endIndex = index;
		}
	}
	
	private static AbstractJsonHub parse(String str) {
		
		SeekCharResult r = seekNextChar(str, 0);
		
		SeekValueResult vr;
		
		if ( JsonStructuralChar.QUOT.match(r.c) ) {
			
			vr = parseStringValue(str, r.index);
			
		} else if ( JsonStructuralChar.ARRAY_BIGIN.match(r.c) ) {
			
			vr = parseArrayValue(str, r.index);
			
		} else if ( JsonStructuralChar.OBJECT_BIGIN.match(r.c) ) {
			
			vr = parseObjectValue(str, r.index);
			
		} else {
			
			vr = parseNotStructuralValue(str, r.index);
			
			if ( vr.endIndex < 0 ) {
				
				return vr.value;
				
			} else {
				
				throw new JsonHubParseException("Value is not Single \"" + str + "\"");
			}
		}
		
		if ( seekNextChar(str, vr.endIndex).index < 0 ) {
			
			return vr.value;
			
		} else {
			
			throw new JsonHubParseException("Value is not Single \"" + str + "\"");
		}
	}
	
	private static SeekValueResult parseNotStructuralValue(String str, int fromIndex) {
		
		SeekCharResult r = seekNextEndDelimiter(str, fromIndex);
		
		String s;
		
		if ( r.index < 0 ) {
			
			if ( fromIndex >= 0 ) {
				
				s = str.substring(fromIndex).trim();
			
			} else {
				
				throw new JsonHubIndexOutOfBoundsException();
			}
			
		} else {
			
			s = str.substring(fromIndex, r.index).trim();
		}
		
		if ( JsonLiteral.NULL.match(s) ) {
			
			return new SeekValueResult(
					JsonHubBuilder.getInstance().nullValue()
					, r.index);
			
		} else if ( JsonLiteral.TRUE.match(s) ) {
			
			return new SeekValueResult(
					JsonHubBuilder.getInstance().trueValue()
					, r.index);
			
		} else if ( JsonLiteral.FALSE.match(s) ) {
			
			return new SeekValueResult(
					JsonHubBuilder.getInstance().falseValue()
					, r.index);
			
		} else {
			
			return new SeekValueResult(
					parseNumberValue(s)
					, r.index);
		}
	}
	
	private static JsonString parseString(String str) {
		String s = str.trim();
		return JsonString.escaped(s.substring(1, (s.length() - 1)));
	}
	
	private static SeekValueResult parseStringValue(String str, int fromIndex) {
		
		int endIndex = seekEndIndexOfString(str, fromIndex);
		
		return new SeekValueResult(
				JsonHubBuilder.getInstance().string(
						parseString(str.substring(fromIndex, endIndex)))
				, endIndex);
	}
	
	private static SeekValueResult parseArrayValue(String str, int fromIndex) {
		
		final List<AbstractJsonHub> ll = new ArrayList<>();
		
		boolean first = true;
		
		for (int i = (fromIndex + 1), len = str.length(); i < len;) {
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( first ) {
					
					first = false;
					
					if ( JsonStructuralChar.ARRAY_END.match(r.c) ) {
						
						return new SeekValueResult(
								JsonHubBuilder.getInstance().array(ll)
								, r.index + 1);
						
					}
				}
				
				if ( JsonStructuralChar.QUOT.match(r.c) ) {
					
					SeekValueResult vr = parseStringValue(str, r.index);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.ARRAY_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseArrayValue(str, r.index);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.OBJECT_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseObjectValue(str, r.index);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if (JsonStructuralChar.SEPARATOR_NAME.match(r.c) || JsonStructuralChar.SEPARATOR_VALUE.match(r.c)) {
					
					throw new JsonHubParseException("Value is empty \"" + str + "\"");
					
				} else {
					
					SeekValueResult vr = parseNotStructuralValue(str, r.index);
					
					if ( vr.endIndex < 0 ) {
						
						throw new JsonHubParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
						
					} else {
						
						ll.add(vr.value);
						i = vr.endIndex;
					}
				}
			}
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( JsonStructuralChar.SEPARATOR_VALUE.match(r.c) ) {
					
					i = r.index + 1;
					
				} else if ( JsonStructuralChar.ARRAY_END.match(r.c) ) {
					
					return new SeekValueResult(
							JsonHubBuilder.getInstance().array(ll)
							, r.index + 1);
					
				} else {
					
					throw new JsonHubParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
				}
			}
		}
		
		throw new JsonHubParseException("Not found end-of-ARRAY. fromIndex: " + fromIndex + " \"" + str + "\"");
	}
	
	private static SeekValueResult parseObjectValue(String str, int fromIndex) {
		
		final JsonHubBuilder jhb = JsonHubBuilder.getInstance();
		
		final Collection<JsonObjectPair> pairs = new ArrayList<>();
		
		boolean first = true;
		
		for (int i = (fromIndex + 1), len = str.length(); i < len;) {
			
			if ( first ) {
				
				first = false;
				
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( JsonStructuralChar.OBJECT_END.match(r.c) ) {
					
					return new SeekValueResult(
							jhb.object(pairs)
							, r.index + 1);
				}
				
				i = r.index;
			}
			
			int nameStartIndex = seekIndexOfNextQuot(str, i);
			int nameEndIndex = seekEndIndexOfString(str, nameStartIndex);
			
			JsonString js = parseString(str.substring(nameStartIndex, nameEndIndex));
			
			i = seekIndexOfNextColon(str, nameEndIndex) + 1;
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( JsonStructuralChar.QUOT.match(r.c) ) {
					
					SeekValueResult vr = parseStringValue(str, r.index);
					pairs.add(jhb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.ARRAY_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseArrayValue(str, r.index);
					pairs.add(jhb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.OBJECT_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseObjectValue(str, r.index);
					pairs.add(jhb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if (JsonStructuralChar.SEPARATOR_NAME.match(r.c) || JsonStructuralChar.SEPARATOR_VALUE.match(r.c)) {
					
					throw new JsonHubParseException("Value is empty. index: " + r.index + " \"" + str + "\"");
					
				} else {
					
					SeekValueResult vr = parseNotStructuralValue(str, r.index);
					
					if ( vr.endIndex < 0 ) {
						
						throw new JsonHubParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
						
					} else {
						
						pairs.add(jhb.pair(js, vr.value));
						i = vr.endIndex;
					}
				}
			}
			
			{
				SeekCharResult r  = seekNextChar(str, i);
				
				if ( JsonStructuralChar.SEPARATOR_VALUE.match(r.c) ) {
					
					i = r.index + 1;
					
				} else if ( JsonStructuralChar.OBJECT_END.match(r.c) ) {
					
					return new SeekValueResult(
							jhb.object(pairs)
							, r.index + 1);
					
				} else {
					
					throw new JsonHubParseException("Not found end-of-value. index: " + r.index + " \"" + str + "\"");
				}
			}
		}
		
		throw new JsonHubParseException("Not found end-of-OBJECT. fromIndex: " + fromIndex + " \"" + str + "\"");
	}
	
	private static NumberJsonHub parseNumberValue(String str) {
		return JsonHubBuilder.getInstance().number(str);
	}
	
	private static int seekIndexOfNextQuot(String str, int fromIndex) {
		SeekCharResult r = seekNextChar(str, fromIndex);
		if ((r.index >= 0) && JsonStructuralChar.QUOT.match(r.c) ) {
			return r.index;
		} else {
			throw new JsonHubParseException("Not found Quot. fromIndex: " + fromIndex + " \"" + str + "\"");
		}
	}
	
	private static int seekIndexOfNextColon(String str, int fromIndex) {
		SeekCharResult r = seekNextChar(str, fromIndex);
		if ((r.index >= 0) && JsonStructuralChar.SEPARATOR_NAME.match(r.c)) {
			return r.index;
		} else {
			throw new JsonHubParseException("Not found \":\" fromIndex: " + fromIndex + " \"" + str + "\"");
		}
	}
	
	private static int seekEndIndexOfString(String str, int fromIndex) {
		
		for (int i = (fromIndex + 1), len = str.length(); i < len; ++i) {
			
			char c = str.charAt(i);
			
			if ( JsonStructuralChar.ESCAPE.match(c) ) {
				++i;
				continue;
			}
			
			if ( JsonStructuralChar.QUOT.match(c) ) {
				return i + 1;
			}
		}
		
		throw new JsonHubParseException("Not found end-of-STRING. fromIndex: " + fromIndex + " \"" + str + "\"");
	}
	
	
	private static final char CHAR_WS = 0x0020;
	
	private static SeekCharResult seekNextChar(String str, int fromIndex) {
		
		if ( fromIndex >= 0 ) {
			
			for (int i = fromIndex, len = str.length(); i < len; ++i) {
				
				char c = str.charAt(i);
				
				if ( c > CHAR_WS ) {
					return new SeekCharResult(c, i);
				}
			}
		}
		
		return new SeekCharResult(CHAR_WS, -1);
	}
	
	private static final JsonStructuralChar[] delimiters = new JsonStructuralChar[]{
			JsonStructuralChar.SEPARATOR_VALUE,
			JsonStructuralChar.ARRAY_END,
			JsonStructuralChar.OBJECT_END
	};
	
	private static SeekCharResult seekNextEndDelimiter(String str, int fromIndex) {
		
		if ( fromIndex >= 0 ) {
			
			for (int i = fromIndex, len = str.length(); i < len; ++i) {
				
				char c = str.charAt(i);
				
				for (JsonStructuralChar d : delimiters ) {
					
					if ( d.match(c) ) {
						return new SeekCharResult(c, i);
					}
				}
			}
		}
		
		return new SeekCharResult(CHAR_WS, -1);
	}
	
}
