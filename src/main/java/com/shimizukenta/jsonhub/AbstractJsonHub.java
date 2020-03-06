package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Immutable Object
 *
 */
abstract public class AbstractJsonHub implements JsonHub, Serializable {
	
	private static final long serialVersionUID = -8854276210327173340L;
	
	private byte[] toBytesCache;
	private byte[] toBytesExcludeNullValueInObjectCache;
	
	public AbstractJsonHub() {
		toBytesCache = null;
		toBytesExcludeNullValueInObjectCache = null;
	}
	
	@Override
	public byte[] getBytes() {
		byte[] bs = toBytesCache();
		return Arrays.copyOf(bs, bs.length);
	}
	
	@Override
	public void writeBytes(OutputStream strm) throws IOException {
		strm.write(toBytesCache());
	}
	
	@Override
	public byte[] getBytesExcludedNullValueInObject() {
		byte[] bs = this.toBytesExcludeNullValueInObjectCache();
		return Arrays.copyOf(bs, bs.length);
	}
	
	@Override
	public void writeBytesExcludedNullValueInObject(OutputStream strm) throws IOException {
		strm.write(toBytesExcludeNullValueInObjectCache());
	}
	
	private byte[] toBytesCache() {
		
		synchronized ( this ) {
			
			if ( toBytesCache == null ) {
				toBytesCache = toJson().getBytes(StandardCharsets.UTF_8);;
			}
			
			return toBytesCache;
		}
	}
	
	private byte[] toBytesExcludeNullValueInObjectCache() {
		
		synchronized ( this ) {
			
			if ( toBytesExcludeNullValueInObjectCache == null ) {
				toBytesExcludeNullValueInObjectCache = toJsonExcludedNullValueInObject().getBytes(StandardCharsets.UTF_8);
			}
			
			return toBytesExcludeNullValueInObjectCache;
		}
	}
	
}
