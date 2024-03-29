package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import com.shimizukenta.jsonhub.impl.AbstractJsonHubPrettyPrinter;
import com.shimizukenta.jsonhub.impl.JsonHubCompactPrettyPrinter;
import com.shimizukenta.jsonhub.impl.JsonHubNoneNullValueInObjectCompactPrettyPrinter;

/**
 * This interface is implements of pretty-printing JSON.
 * 
 * <ul>
 * <li>To get default-printer, {@link #getDefaultPrinter()}.</li>
 * <li>To get compact-printer, {@link #getCompactPrinter()}.</li>
 * <li>To get compact-and-exclude-null-value-in-object-printer, {@link #getNoneNullValueInObjectCompactPrinter()}.</li>
 * <li>To get custom-printer, {@link #newPrinter(JsonHubPrettyPrinterConfig)}.</li>
 * </ul>
 * <ul>
 * <li>To get Pretty-JSON-String, {@link #print(JsonHub)}.</li>
 * <li>To print to writer, {@link #print(JsonHub, Writer)}.</li>
 * <li>To print to file, {@link #print(JsonHub, Path)}.</li>
 * <li>To print to file with options, {@link #print(JsonHub, Path, OpenOption...)}.</li>
 * </ul>
 * 
 * @author kenta-shimizu
 * @see JsonHubPrettyPrinterConfig
 *
 */
public interface JsonHubPrettyPrinter {
	
	/**
	 * Write to writer
	 * 
	 * @param value the JsonHub
	 * @param writer output writer
	 * @throws IOException if IO failed
	 */
	public void print(JsonHub value, Writer writer) throws IOException;
	
	/**
	 * Write to File.
	 * 
	 * @param value the JsonHub
	 * @param path output file-path
	 * @throws IOException if IO failed
	 */
	public void print(JsonHub value, Path path) throws IOException;
	
	/**
	 * Write to File with options.
	 * 
	 * @param value the JsonHub
	 * @param path output file-path
	 * @param options the File-open-options.
	 * @throws IOException if IO failed
	 */
	public void print(JsonHub value, Path path, OpenOption... options) throws IOException;
	
	/**
	 * Returns Pritty-JSON-String.
	 * 
	 * @param value the JsonHub
	 * @return Pretty-JSON-String
	 */
	public String print(JsonHub value);
	
	
	/**
	 * Returns Default-pretty-printer instance
	 * 
	 * @return Default-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter getDefaultPrinter() {
		return AbstractJsonHubPrettyPrinter.getDefaultPrinter();
	}
	
	/**
	 * Returns Customized-pretty-printer instance.
	 * 
	 * @param config the config
	 * @return Customized-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter newPrinter(JsonHubPrettyPrinterConfig config) {
		return AbstractJsonHubPrettyPrinter.newPrinter(config);
	}
	
	/**
	 * Returns Compact-pretty-printer instance.
	 * 
	 * @return Compact-JSON-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter getCompactPrinter() {
		return JsonHubCompactPrettyPrinter.getInstance();
	}
	
	/**
	 * Returns Compact-and-exclude-null-value-in-object-pretty-printer instance.
	 * 
	 * @return Compact-and-exclude-null-value-in-object-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter getNoneNullValueInObjectCompactPrinter() {
		return JsonHubNoneNullValueInObjectCompactPrettyPrinter.getInstance();
	}
	
}
