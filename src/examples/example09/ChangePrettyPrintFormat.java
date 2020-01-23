package example09;

import java.util.Arrays;
import java.util.List;

import jsonHub.JsonHub;
import jsonHub.JsonHubPrettyPrinterConfig;

public class ChangePrettyPrintFormat {
	
	public int num;
	public String str;
	public boolean bool;
	public Object nul;
	public List<String> list;
	
	protected float not_parse_because_not_public;
	public static long not_parse_because_static = -1L;
	
	public ChangePrettyPrintFormat() {
		
		num = 1;
		str = "STRING";
		bool = true;
		nul = null;
		list = Arrays.asList("a", "b", "c");
		
		not_parse_because_not_public = -1.0F;
	}
	
	public static void main(String[] args) {
		
		ChangePrettyPrintFormat pojo = new ChangePrettyPrintFormat();
		
		JsonHub jh = JsonHub.fromPojo(pojo);
		
		{
			System.out.println("Defualt prettyPrint");
			System.out.println(jh.prettyPrint());
		}
		
		System.out.println();
		
		{
			JsonHubPrettyPrinterConfig config = new JsonHubPrettyPrinterConfig();
			
			config.indent("\t");
			config.lineSeparator(System.lineSeparator());
			config.prefixValueSeparator("");
			config.suffixValueSeparator(" ");
			config.prefixNameSeparator(" ");
			config.suffixNameSeparator(" ");
			config.lineSeparateBeforeValueSeparator(true);
			config.lineSeparateAfterValueSeparator(false);
			config.lineSeparateIfBlank(true);
			
			System.out.println("Changed prettyPrint");
			System.out.println(jh.prettyPrint(config));
		}
	}

}
