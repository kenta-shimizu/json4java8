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
	 * @param json the charsequence
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
	 * @param reader the reader of JSON
	 * @return parsed JsonHub instance
	 * @throws IOException if IO-Exception
	 * @throws JsonHubParseException if parse failed
	 */
	public static JsonHub fromJson(Reader reader) throws IOException {
		try (
				CharArrayWriter writer = new CharArrayWriter();
				) {
			
			for ( ;; ) {
				
				int r = reader.read();
				
				if ( r < 0 ) {
					return fromJson(writer.toString());
				}
				
				writer.write(r);
			}
		}
	}
	
	/**
	 * Returns parsed JsonHub instance from read file.
	 * 
	 * @param path JSON file path
	 * @return parsed JsonHub instance
	 * @throws IOException if IO-Exception
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
	 * @throws IOException if IO-Exception
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
	
	private static class SeekValueResult {
		
		private final AbstractJsonHub value;
		private final int endIndex;
		
		private SeekValueResult(AbstractJsonHub v, int index) {
			this.value = v;
			this.endIndex = index;
		}
	}
	
	private static AbstractJsonHub parse(String str) {
		
		FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, 0);
		
		SeekValueResult vr;
		
		if ( JsonStructuralChar.QUOT.match(r.c) ) {
			
			vr = parseStringValue(str, r.pos);
			
		} else if ( JsonStructuralChar.ARRAY_BIGIN.match(r.c) ) {
			
			vr = parseArrayValue(str, r.pos);
			
		} else if ( JsonStructuralChar.OBJECT_BIGIN.match(r.c) ) {
			
			vr = parseObjectValue(str, r.pos);
			
		} else {
			
			vr = parseNotStructuralValue(str, r.pos);
			
			if ( vr.endIndex < 0 ) {
				
				return vr.value;
				
			} else {
				
				throw new JsonHubParseException("Value is not Single \"" + str + "\"");
			}
		}
		
		if ( FindChars.nextIgnoreWhiteSpace(str, vr.endIndex).pos < 0 ) {
			
			return vr.value;
			
		} else {
			
			throw new JsonHubParseException("Value is not Single \"" + str + "\"");
		}
	}
	
	private static SeekValueResult parseNotStructuralValue(String str, int fromIndex) {
		
		FindCharResult r = FindChars.next(str, fromIndex,
				JsonStructuralChar.SEPARATOR_VALUE.chr(),
				JsonStructuralChar.ARRAY_END.chr(),
				JsonStructuralChar.OBJECT_END.chr());
		
		String s;
		
		if ( r.pos < 0 ) {
			
			if ( fromIndex >= 0 ) {
				
				s = str.substring(fromIndex).trim();
			
			} else {
				
				throw new JsonHubIndexOutOfBoundsException();
			}
			
		} else {
			
			s = str.substring(fromIndex, r.pos).trim();
		}
		
		if ( JsonLiteral.NULL.match(s) ) {
			
			return new SeekValueResult(
					JsonHubBuilder.getInstance().nullValue()
					, r.pos);
			
		} else if ( JsonLiteral.TRUE.match(s) ) {
			
			return new SeekValueResult(
					JsonHubBuilder.getInstance().trueValue()
					, r.pos);
			
		} else if ( JsonLiteral.FALSE.match(s) ) {
			
			return new SeekValueResult(
					JsonHubBuilder.getInstance().falseValue()
					, r.pos);
			
		} else {
			
			return new SeekValueResult(
					parseNumberValue(s)
					, r.pos);
		}
	}
	
	private static JsonString parseString(String str) {
		String s = str.trim();
		return JsonString.escaped(s.substring(1, (s.length() - 1)));
	}
	
	private static SeekValueResult parseStringValue(String str, int fromIndex) {
		
		int endIndex = seekEndIndexOfString(str, fromIndex + 1);
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
				FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, i);
				
				if ( first ) {
					
					first = false;
					
					if ( JsonStructuralChar.ARRAY_END.match(r.c) ) {
						
						return new SeekValueResult(
								JsonHubBuilder.getInstance().array(ll)
								, r.pos + 1);
						
					}
				}
				
				if ( JsonStructuralChar.QUOT.match(r.c) ) {
					
					SeekValueResult vr = parseStringValue(str, r.pos);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.ARRAY_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseArrayValue(str, r.pos);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.OBJECT_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseObjectValue(str, r.pos);
					ll.add(vr.value);
					i = vr.endIndex;
					
				} else if (JsonStructuralChar.SEPARATOR_NAME.match(r.c) || JsonStructuralChar.SEPARATOR_VALUE.match(r.c)) {
					
					throw new JsonHubParseException("Value is empty \"" + str + "\"");
					
				} else {
					
					SeekValueResult vr = parseNotStructuralValue(str, r.pos);
					
					if ( vr.endIndex < 0 ) {
						
						throw new JsonHubParseException("Not found end-of-value. index: " + r.pos + " \"" + str + "\"");
						
					} else {
						
						ll.add(vr.value);
						i = vr.endIndex;
					}
				}
			}
			
			{
				FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, i);
				
				if ( JsonStructuralChar.SEPARATOR_VALUE.match(r.c) ) {
					
					i = r.pos + 1;
					
				} else if ( JsonStructuralChar.ARRAY_END.match(r.c) ) {
					
					return new SeekValueResult(
							JsonHubBuilder.getInstance().array(ll)
							, r.pos + 1);
					
				} else {
					
					throw new JsonHubParseException("Not found end-of-value. index: " + r.pos + " \"" + str + "\"");
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
				
				FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, i);
				
				if ( JsonStructuralChar.OBJECT_END.match(r.c) ) {
					return new SeekValueResult(jhb.object(pairs), r.pos + 1);
				}
				
				i = r.pos;
			}
			
			int nameStartIndex = seekIndexOfNextQuot(str, i);
			int nameEndIndex = seekEndIndexOfString(str, nameStartIndex + 1);
			
			JsonString js = parseString(str.substring(nameStartIndex, nameEndIndex));
			
			i = seekIndexOfNextColon(str, nameEndIndex) + 1;
			
			{
				FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, i);
				
				if ( JsonStructuralChar.QUOT.match(r.c) ) {
					
					SeekValueResult vr = parseStringValue(str, r.pos);
					pairs.add(jhb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.ARRAY_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseArrayValue(str, r.pos);
					pairs.add(jhb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if ( JsonStructuralChar.OBJECT_BIGIN.match(r.c) ) {
					
					SeekValueResult vr = parseObjectValue(str, r.pos);
					pairs.add(jhb.pair(js, vr.value));
					i = vr.endIndex;
					
				} else if (JsonStructuralChar.SEPARATOR_NAME.match(r.c) || JsonStructuralChar.SEPARATOR_VALUE.match(r.c)) {
					
					throw new JsonHubParseException("Value is empty. index: " + r.pos + " \"" + str + "\"");
					
				} else {
					
					SeekValueResult vr = parseNotStructuralValue(str, r.pos);
					
					if ( vr.endIndex < 0 ) {
						
						throw new JsonHubParseException("Not found end-of-value. index: " + r.pos + " \"" + str + "\"");
						
					} else {
						
						pairs.add(jhb.pair(js, vr.value));
						i = vr.endIndex;
					}
				}
			}
			
			{
				FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, i);
				
				if ( JsonStructuralChar.SEPARATOR_VALUE.match(r.c) ) {
					
					i = r.pos + 1;
					
				} else if ( JsonStructuralChar.OBJECT_END.match(r.c) ) {
					
					return new SeekValueResult(
							jhb.object(pairs)
							, r.pos + 1);
					
				} else {
					
					throw new JsonHubParseException("Not found end-of-value. index: " + r.pos + " \"" + str + "\"");
				}
			}
		}
		
		throw new JsonHubParseException("Not found end-of-OBJECT. fromIndex: " + fromIndex + " \"" + str + "\"");
	}
	
	private static NumberJsonHub parseNumberValue(String str) {
		
		
		//TODO
		return JsonHubBuilder.getInstance().number(str);
	}
	
	private static int seekIndexOfNextQuot(String str, int fromIndex) {
		FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, fromIndex);
		if ((r.pos >= 0) && JsonStructuralChar.QUOT.match(r.c) ) {
			return r.pos;
		} else {
			throw new JsonHubParseException("Not found Quot. fromIndex: " + fromIndex + " \"" + str + "\"");
		}
	}
	
	private static int seekIndexOfNextColon(String str, int fromIndex) {
		FindCharResult r = FindChars.nextIgnoreWhiteSpace(str, fromIndex);
		if ((r.pos >= 0) && JsonStructuralChar.SEPARATOR_NAME.match(r.c)) {
			return r.pos;
		} else {
			throw new JsonHubParseException("Not found \":\" fromIndex: " + fromIndex + " \"" + str + "\"");
		}
	}
	
	private static int seekEndIndexOfString(String str, int fromIndex) {
		
		FindCharResult r = FindChars.nextIgnoreEscape(str, fromIndex, JsonStructuralChar.QUOT.chr());
		if ( r.pos < 0 ) {
			throw new JsonHubParseException("Not found end-of-STRING. fromIndex: " + fromIndex + " \"" + str + "\"");
		} else {
			return r.pos + 1;
		}
	}
	
}
