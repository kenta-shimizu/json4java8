package jsonValue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JsonValuePrettyPrinter {
	
	private JsonValuePrettyPrinterConfig config;
	
	public JsonValuePrettyPrinter() {
		this(new JsonValuePrettyPrinterConfig());
	}
	
	public JsonValuePrettyPrinter(JsonValuePrettyPrinterConfig config) {
		this.config = config;
	}
	
	/**
	 * set-config
	 * 
	 * @param config
	 */
	public void config(JsonValuePrettyPrinterConfig config) {
		synchronized ( this ) {
			this.config = config;
		}
	}
	
	/**
	 * get-config
	 * 
	 * @return config
	 */
	public JsonValuePrettyPrinterConfig config() {
		synchronized ( this ) {
			return config;
		}
	}
	
	/**
	 * Pretty-Print-JSON
	 * 
	 * @param JsonValue
	 * @param writer
	 * @throws IOException
	 */
	public void print(JsonValue v, Writer writer) throws IOException {
		
		synchronized ( this ) {
			print(v, writer, 0);
		}
	}
	
	/**
	 * 
	 * @param JsonValue
	 * @return Pretty-Print-JSON
	 */
	public String print(JsonValue v) {
		
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
	
	private void print(JsonValue v, Writer writer, int level) throws IOException {
		
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
			writer.write(JsonStructuralChar.ARRAY_LEFT.str);
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for (JsonValue jv : v.values()) {
					
					if ( f ) {
						writeComma(writer);
					} else {
						f = true;
					}
					
					print(jv, writer, deepLevel);
				}
				
				writeLineSeparator(writer);
				writeIndent(writer, level);
			}
			
			writer.write(JsonStructuralChar.ARRAY_RIGHT.str);
			
			break;
		}
		case OBJECT: {
			
			writeIndent(writer, level);
			writer.write(JsonStructuralChar.OBJECT_LEFT.str);
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for ( JsonObjectPair pair : v.objectPairs() ) {
					
					if ( f ) {
						writeComma(writer);
					} else {
						f = true;
					}
					
					printObjectPair(pair, writer, deepLevel);
				}
				
				writeLineSeparator(writer);
				writeIndent(writer, level);
			}
			
			writer.write(JsonStructuralChar.OBJECT_RIGHT.str);
			
			break;
		}
		}
	}
	
	private void printObjectPair(JsonObjectPair pair, Writer writer, int level) throws IOException {
		
		JsonValue v = pair.value();
		
		writeIndent(writer, level);
		writeObjectName(writer, pair);
		writeColon(writer);
		
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
			
			writer.write(JsonStructuralChar.ARRAY_LEFT.str);
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for (JsonValue jv : v.values()) {
					
					if ( f ) {
						writeComma(writer);
					} else {
						f = true;
					}
					
					print(jv, writer, deepLevel);
				}
				
				writeLineSeparator(writer);
				writeIndent(writer, level);
			}
			
			writer.write(JsonStructuralChar.ARRAY_RIGHT.str);

			break;
		}
		case OBJECT: {
			
			writer.write(JsonStructuralChar.OBJECT_LEFT.str);
			
			if ( ! v.isEmpty() ) {
				
				writeLineSeparator(writer);
				
				int deepLevel = level + 1;
				
				boolean f = false;
				
				for ( JsonObjectPair p : v.objectPairs() ) {
					
					if ( f ) {
						writeComma(writer);
					} else {
						f = true;
					}
					
					printObjectPair(p, writer, deepLevel);
				}
				
				writeLineSeparator(writer);
				writeIndent(writer, level);
			}
			
			writer.write(JsonStructuralChar.OBJECT_RIGHT.str);

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
	
	private void writeComma(Writer writer) throws IOException {
		writer.write(config().prefixComma());
		writer.write(JsonStructuralChar.COMMA.str);
		writer.write(config().suffixComma());
	}
	
	private void writeColon(Writer writer) throws IOException {
		writer.write(config().prefixColon());
		writer.write(JsonStructuralChar.COLON.str);
		writer.write(config().suffixColon());
	}
	
	private void writeObjectName(Writer writer, JsonObjectPair pair) throws IOException {
		writer.write(JsonStructuralChar.QUOT.str);
		writer.write(pair.name().escaped());
		writer.write(JsonStructuralChar.QUOT.str);
	}
	
}
