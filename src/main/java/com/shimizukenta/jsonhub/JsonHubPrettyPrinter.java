package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * This interface is implements of pretty-printing JSON.
 * 
 * <p>
 * To get default-printer, {@link #getDefaultPrinter()}.<br />
 * To get compact-printer, {@link #getCompactPrinter()}.<br />
 * To get compact-and-exclude-null-value-in-object-printer, {@link #getNoneNullValueInObjectCompactPrinter()}.<br />
 * To get custom-printer, {@link #newPrinter(JsonHubPrettyPrinterConfig)}.<br />
 * </p>
 * <p>
 * To get Pretty-JSON-String, {@link #print(JsonHub)}.<br />
 * To print to writer, {@link #print(JsonHub, Writer)}.<br />
 * To print to file, {@link #print(JsonHub, Path)}.<br />
 * To print to file with options, {@link #print(JsonHub, Path, OpenOption...)}.<br />
 * </p>
 * 
 * @author kenta-shimizu
 *
 */
public interface JsonHubPrettyPrinter {
	
	/**
	 * Write to writer
	 * 
	 * @param v
	 * @param writer
	 * @throws IOException
	 */
	public void print(JsonHub v, Writer writer) throws IOException;
	
	/**
	 * Write to File.
	 * 
	 * @param v
	 * @param path
	 * @throws IOException
	 */
	public void print(JsonHub v, Path path) throws IOException;
	
	/**
	 * Write to File with options.
	 * 
	 * @param v
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	public void print(JsonHub v, Path path, OpenOption... options) throws IOException;
	
	/**
	 * Pritty-JSON-String getter
	 * 
	 * @param v
	 * @return Pretty-JSON-String
	 */
	public String print(JsonHub v);
	
	
	/**
	 * Default-pretty-printer instance getter.
	 * 
	 * @return Default-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter getDefaultPrinter() {
		return AbstractJsonHubPrettyPrinter.getDefaultPrinter();
	}
	
	/**
	 * Customized-pretty-printer instance getter.
	 * 
	 * @param config
	 * @return Customized-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter newPrinter(JsonHubPrettyPrinterConfig config) {
		return AbstractJsonHubPrettyPrinter.newPrinter(config);
	}
	
	/**
	 * Compact-pretty-printer instance getter
	 * 
	 * @return Compact-JSON-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter getCompactPrinter() {
		return JsonHubCompactPrettyPrinter.getInstance();
	}
	
	/**
	 * Compact-and-exclude-null-value-in-object-pretty-printer instance getter.
	 * 
	 * @return Compact-and-exclude-null-value-in-object-pretty-printer instance
	 */
	public static JsonHubPrettyPrinter getNoneNullValueInObjectCompactPrinter() {
		return JsonHubNoneNullValueInObjectCompactPrettyPrinter.getInstance();
	}
	
}
