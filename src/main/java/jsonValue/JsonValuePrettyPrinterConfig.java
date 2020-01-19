package jsonValue;

public class JsonValuePrettyPrinterConfig {
	
	private static final String defaultIndent = "  ";
	private static final String defaultLineSeparator = System.lineSeparator();
	private static final String defaultPrefixComma = "";
	private static final String defaultSuffixComma = System.lineSeparator();
	private static final String defaultPrefixColon = "";
	private static final String defaultSuffixColon = " ";
	
	
	private String indent;
	private String lineSeparator;
	private String prefixComma;
	private String suffixComma;
	private String prefixColon;
	private String suffixColon;
	
	public JsonValuePrettyPrinterConfig() {
		this.indent = defaultIndent;
		this.lineSeparator = defaultLineSeparator;
		this.prefixComma = defaultPrefixComma;
		this.suffixComma = defaultSuffixComma;
		this.prefixColon = defaultPrefixColon;
		this.suffixColon = defaultSuffixColon;
	}
	
	
	public void indent(CharSequence cs) {
		synchronized ( this ) {
			this.indent = cs.toString();
		}
	}
	
	public String indent() {
		synchronized ( this ) {
			return this.indent;
		}
	}
	
	public void lineSeparator(CharSequence cs) {
		synchronized ( this ) {
			this.lineSeparator = cs.toString();
		}
	}
	
	public String lineSeparator() {
		synchronized ( this ) {
			return this.lineSeparator;
		}
	}
	
	public void prefixComma(CharSequence cs) {
		synchronized ( this ) {
			this.prefixComma = cs.toString();
		}
	}
	
	public String prefixComma() {
		synchronized ( this ) {
			return this.prefixComma;
		}
	}
	
	public void suffixComma(CharSequence cs) {
		synchronized ( this ) {
			this.suffixComma = cs.toString();
		}
	}
	
	public String suffixComma() {
		synchronized ( this ) {
			return this.suffixComma;
		}
	}
	
	public void prefixColon(CharSequence cs) {
		synchronized ( this ) {
			this.prefixColon = cs.toString();
		}
	}
	
	public String prefixColon() {
		synchronized ( this ) {
			return this.prefixColon;
		}
	}
	
	public void suffixColon(CharSequence cs) {
		synchronized ( this ) {
			this.suffixColon = cs.toString();
		}
	}
	
	public String suffixColon() {
		synchronized ( this ) {
			return this.suffixColon;
		}
	}

}
