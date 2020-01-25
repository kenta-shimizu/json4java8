package jsonHub;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class JsonHubPrettyPrinter {
	
	private JsonHubPrettyPrinterConfig config;
	
	public JsonHubPrettyPrinter() {
		this(new JsonHubPrettyPrinterConfig());
	}
	
	public JsonHubPrettyPrinter(JsonHubPrettyPrinterConfig config) {
		this.config = config;
	}
	
	/**
	 * set-config
	 * 
	 * @param config
	 */
	public void config(JsonHubPrettyPrinterConfig config) {
		synchronized ( this ) {
			this.config = config;
		}
	}
	
	private JsonHubPrettyPrinterConfig config() {
		synchronized ( this ) {
			return config;
		}
	}
	
	private static class SingletonHolder {
		private static final JsonHubPrettyPrinter inst = new JsonHubPrettyPrinter();
	}
	
	public static JsonHubPrettyPrinter getDefaultPrinter() {
		return SingletonHolder.inst;
	}
	
	/**
	 * Pretty-Print-JSON
	 * 
	 * @param JsonHub
	 * @param writer
	 * @throws IOException
	 */
	public void print(JsonHub v, Writer writer) throws IOException {
		
		synchronized ( this ) {
			print(v, writer, 0);
		}
	}
	
	/**
	 * write to File
	 * 
	 * @param v
	 * @param path
	 * @throws IOException
	 */
	public void print(JsonHub v, Path path) throws IOException {
		
		try (
				BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
				) {
			
			print(v, bw);
		}
	}
	
	/**
	 * write to File
	 * 
	 * @param v
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	public void print(JsonHub v, Path path, OpenOption... options) throws IOException {
		
		try (
				BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, options);
				) {
			
			print(v, bw);
		}
	}
	
	/**
	 * 
	 * @param JsonHub
	 * @return Pretty-Print-JSON
	 */
	public String print(JsonHub v) {
		
		synchronized ( this ) {
			
			try (
					StringWriter sw = new StringWriter();
					) {
				
				print(v, sw);
				return sw.toString();
			}
			catch ( IOException notHappen ) {
				throw new RuntimeException(notHappen);
			}
		}
	}
	
	private void print(JsonHub v, Writer writer, int level) throws IOException {
		
		switch ( v.type() ) {
		case NULL:
		case TRUE:
		case FALSE:
		case STRING:
		case NUMBER: {
			
			v.toJson(writer);
			break;
		}
		case ARRAY: {
			
			writer.write(JsonStructuralChar.ARRAY_BIGIN.str());
			
			if ( v.isEmpty() ) {
				
				writeLineSeparatorIfBlank(writer, level);
				
			} else {
				
				int deepLevel = level + 1;
				
				writeLineSeparator(writer);
				writeIndent(writer, deepLevel);
				
				boolean f = false;
				
				for (JsonHub jh : v.values()) {
					
					if ( f ) {
						writeValueSeparator(writer, deepLevel);
					} else {
						f = true;
					}
					
					print(jh, writer, deepLevel);
				}
				
				writeLineSeparator(writer);
				writeIndent(writer, level);
			}
			
			writer.write(JsonStructuralChar.ARRAY_END.str());
			
			break;
		}
		case OBJECT: {
			
			writer.write(JsonStructuralChar.OBJECT_BIGIN.str());
			
			if ( v.isEmpty() ) {
				
				writeLineSeparatorIfBlank(writer, level);
				
			} else {
				
				int deepLevel = level + 1;
				
				writeLineSeparator(writer);
				writeIndent(writer, deepLevel);
				
				boolean f = false;
				
				for ( JsonObjectPair pair : v.objectPairs() ) {
					
					if ( f ) {
						writeValueSeparator(writer, deepLevel);
					} else {
						f = true;
					}
					
					writeObjectName(writer, pair);
					writeNameSeparator(writer);
					
					print(pair.value(), writer, deepLevel);
				}
				
				writeLineSeparator(writer);
				writeIndent(writer, level);
			}
			
			writer.write(JsonStructuralChar.OBJECT_END.str());
			
			break;
		}
		}
	}
	
	private void writeIndent(Writer writer, int level) throws IOException {
		for (int i = 0; i < level; ++i) {
			writer.write(config().indent());
		}
	}
	
	private void writeLineSeparator(Writer writer) throws IOException {
		writer.write(config().lineSeparator());
	}
	
	private void writeLineSeparatorIfBlank(Writer writer, int level) throws IOException {
		if ( config().lineSeparateIfBlank() ) {
			writeLineSeparator(writer);
			writeIndent(writer, level);
		}
	}
	
	private void writeValueSeparator(Writer writer, int level) throws IOException {
		
		if ( config().lineSeparateBeforeValueSeparator() ) {
			writeLineSeparator(writer);
			writeIndent(writer, level);
		}
		
		writer.write(config().prefixValueSeparator());
		writer.write(JsonStructuralChar.SEPARATOR_VALUE.str());
		writer.write(config().suffixValueSeparator());
		
		if ( config().lineSeparateAfterValueSeparator() ) {
			writeLineSeparator(writer);
			writeIndent(writer, level);
		}
	}
	
	private void writeNameSeparator(Writer writer) throws IOException {
		writer.write(config().prefixNameSeparator());
		writer.write(JsonStructuralChar.SEPARATOR_NAME.str());
		writer.write(config().suffixNameSeparator());
	}
	
	private void writeObjectName(Writer writer, JsonObjectPair pair) throws IOException {
		writer.write(JsonStructuralChar.QUOT.str());
		writer.write(pair.name().escaped());
		writer.write(JsonStructuralChar.QUOT.str());
	}
	
}
