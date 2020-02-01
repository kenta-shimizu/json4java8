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
	
	private static final long serialVersionUID = 3793174804475491315L;
	
	private byte[] toBytesProxy;
	
	public AbstractJsonHub() {
		toBytesProxy = null;
	}
	
	@Override
	public byte[] getBytes() {
		byte[] bs = toBytesProxy();
		return Arrays.copyOf(bs, bs.length);
	}
	
	@Override
	public void writeBytes(OutputStream strm) throws IOException {
		strm.write(toBytesProxy());
	}
	
	private byte[] toBytesProxy() {
		
		synchronized ( this ) {
			
			if ( toBytesProxy == null ) {
				toBytesProxy = toJson().getBytes(StandardCharsets.UTF_8);;
			}
			
			return toBytesProxy;
		}
	}
	
}
