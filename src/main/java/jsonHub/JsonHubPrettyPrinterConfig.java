package jsonHub;

public class JsonHubPrettyPrinterConfig {
	
	private static final String defaultIndent = "  ";
	private static final String defaultLineSeparator = System.lineSeparator();
	private static final String defaultPrefixValueSeparator = "";
	private static final String defaultSuffixValueSeparator = System.lineSeparator();
	private static final String defaultPrefixNameSeparator = "";
	private static final String defaultSuffixNameSeparator = " ";
	
	
	private String indent;
	private String lineSeparator;
	private String prefixValueSeparator;
	private String suffixValueSeparator;
	private String prefixNameSeparator;
	private String suffixNameSeparator;
	
	public JsonHubPrettyPrinterConfig() {
		this.indent = defaultIndent;
		this.lineSeparator = defaultLineSeparator;
		this.prefixValueSeparator = defaultPrefixValueSeparator;
		this.suffixValueSeparator = defaultSuffixValueSeparator;
		this.prefixNameSeparator = defaultPrefixNameSeparator;
		this.suffixNameSeparator = defaultSuffixNameSeparator;
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
	
	public void prefixValueSeparator(CharSequence cs) {
		synchronized ( this ) {
			this.prefixValueSeparator = cs.toString();
		}
	}
	
	public String prefixValueSeparator() {
		synchronized ( this ) {
			return this.prefixValueSeparator;
		}
	}
	
	public void suffixValueSeparator(CharSequence cs) {
		synchronized ( this ) {
			this.suffixValueSeparator = cs.toString();
		}
	}
	
	public String suffixValueSeparator() {
		synchronized ( this ) {
			return this.suffixValueSeparator;
		}
	}
	
	public void prefixNameSeparator(CharSequence cs) {
		synchronized ( this ) {
			this.prefixNameSeparator = cs.toString();
		}
	}
	
	public String prefixNameSeparator() {
		synchronized ( this ) {
			return this.prefixNameSeparator;
		}
	}
	
	public void suffixNameSeparator(CharSequence cs) {
		synchronized ( this ) {
			this.suffixNameSeparator = cs.toString();
		}
	}
	
	public String suffixNameSeparator() {
		synchronized ( this ) {
			return this.suffixNameSeparator;
		}
	}

}
