/**
 * 
 */
package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

import com.shimizukenta.jsonhub.impl.JsoncReaderImpl;

/**
 * This interface is JSONC reader to JsonHub.
 * 
 * <p>
 * Read JSONC and parse to JsonHub.<br />
 * </p>
 * <ul>
 * <li>Comments //</li>
 * <li>Comments /* ...</li>
 * <li>Trailing Comma(,) in Array</li>
 * <li>Trailing Comma(,) in Object</li>
 * </ul>
 * 
 * @author kenta-shimizu
 *
 */
public interface JsoncReader {
	
	/**
	 * Returns parsed JsonHub instance from JSONC String.
	 * 
	 * @param line of JSONC String.
	 * @return parsed JsonHub.
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readLine(CharSequence line) {
		return JsoncReaderImpl.readLine(line);
	}
	
	/**
	 * Returns parsed JsonHub instance from JSONC lines.
	 * 
	 * @param lines of JSONC String 
	 * @return parsed JsonHub.
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readLines(List<? extends CharSequence> lines) {
		return JsoncReaderImpl.readLines(lines);
	}
	
	/**
	 * Returns parsed JsonHub instance from JSONC reader.
	 * 
	 * @param reader of JSONC
	 * @return parsed JsonHub.
	 * @throws IOException if IO failed
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readReader(Reader reader) throws IOException {
		return JsoncReader.readReader(reader);
	}
	
	/**
	 * Returns parsed JsonHub instance from JSONC file.
	 * 
	 * @param path of JSONC file
	 * @return parsed JsonHub.
	 * @throws IOException if IO failed
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readFile(Path path) throws IOException {
		return JsoncReaderImpl.readFile(path);
	}
	
}
