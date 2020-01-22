package jsonHub;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

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
			
			writeIndent(writer, level);
			v.toJson(writer);
			
			break;
		}
		case ARRAY: {
			
			writeIndent(writer, level);
			writer.write(JsonStructuralChar.ARRAY_BIGIN.str());
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for (JsonHub jh : v.values()) {
					
					if ( f ) {
						writeValueSeparator(writer);
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
			
			writeIndent(writer, level);
			writer.write(JsonStructuralChar.OBJECT_BIGIN.str());
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for ( JsonObjectPair pair : v.objectPairs() ) {
					
					if ( f ) {
						writeValueSeparator(writer);
					} else {
						f = true;
					}
					
					printObjectPair(pair, writer, deepLevel);
				}
				
				writeLineSeparator(writer);
				writeIndent(writer, level);
			}
			
			writer.write(JsonStructuralChar.OBJECT_END.str());
			
			break;
		}
		}
	}
	
	private void printObjectPair(JsonObjectPair pair, Writer writer, int level) throws IOException {
		
		JsonHub v = pair.value();
		
		writeIndent(writer, level);
		writeObjectName(writer, pair);
		writeNameSeparator(writer);
		
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
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for (JsonHub jh : v.values()) {
					
					if ( f ) {
						writeValueSeparator(writer);
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
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for ( JsonObjectPair p : v.objectPairs() ) {
					
					if ( f ) {
						writeValueSeparator(writer);
					} else {
						f = true;
					}
					
					printObjectPair(p, writer, deepLevel);
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
	
	private void writeValueSeparator(Writer writer) throws IOException {
		writer.write(config().prefixValueSeparator());
		writer.write(JsonStructuralChar.SEPARATOR_VALUE.str());
		writer.write(config().suffixValueSeparator());
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
