package jsonValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonStringValue extends JsonValue {
	
	private final String v;
	
	private String toJsonProxy;
	
	public JsonStringValue(CharSequence cs) {
		super();
		
		this.v = cs.toString();
		this.toJsonProxy = null;
	}
	
	@Override
	public JsonValueType type() {
		return JsonValueType.STRING;
	}
	
	@Override
	public int length() {
		return v.length();
	}
	
	@Override
	public String toJson() {
		return toJsonProxy();
	}
	
	private String toJsonProxy() {
		
		synchronized ( this ) {
			
			if ( toJsonProxy == null ) {
				toJsonProxy = "\"" + escape(v) + "\"";
			}
			
			return toJsonProxy;
		}
	}
	
	@Override
	public String toString() {
		return v;
	}
	
	@Override
	public boolean equals(Object o) {
		if ( o instanceof JsonStringValue ) {
			return ((JsonStringValue) o).v.equals(v);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return v.hashCode();
	}
	
	
	public static JsonStringValue unescape(String v) {
		
		try (
				ByteArrayOutputStream strm = new ByteArrayOutputStream();
				) {
			
			byte[] bb = v.getBytes(StandardCharsets.UTF_8);
			
			for (int i = 0, len = bb.length; i < len; ++i) {
				
				byte b = bb[i];
				
				if ( b == BACKSLASH ) {
					
					++i;
					
					byte b2 = bb[i];
					
					if ( b2 == UNICODE ) {
						
						byte[] xx = unescapeUnicode(new byte[]{bb[i + 1], bb[i + 2], bb[i + 3], bb[i + 4]});
						strm.write(xx);
						
						i += 4;
						
					} else {
						
						Byte x = EscapeSets.unescape(b2);
						
						if ( x != null ) {
							strm.write(x.byteValue());
						}
					}
					
				} else {
					
					strm.write(b);
				}
			}
			
			return new JsonStringValue(new String(strm.toByteArray(), StandardCharsets.UTF_8));
		}
		catch ( IOException notHappen ) {
			throw new RuntimeException(notHappen);
		}
		catch ( IndexOutOfBoundsException e ) {
			throw new JsonValueIndexOutOfBoundsException("unescape failed \"" + v + "\"");
		}
	}
	
	private static byte[] unescapeUnicode(byte[] bb) {
		
		String s1 = new String(new byte[]{bb[0], bb[1]}, StandardCharsets.UTF_8);
		String s2 = new String(new byte[]{bb[2], bb[3]}, StandardCharsets.UTF_8);
		
		byte b = decodeXXtoByte(s1);
		
		if ( b == 0x00 ) {
			
			return new byte[] {decodeXXtoByte(s2)};
			
		} else {
			
			return new byte[] {b, decodeXXtoByte(s2)};
		}
	}
	
	private static byte decodeXXtoByte(String s) {
		
		try {
			return Byte.parseByte(s, 16);
		}
		catch ( NumberFormatException e ) {
			throw new JsonValueNumberFormatException(s);
		}
	}
}
