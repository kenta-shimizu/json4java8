package example08;

import java.util.Arrays;
import java.util.List;

import jsonHub.JsonHub;
import jsonHub.JsonHubType;
import jsonHub.JsonString;

public class ForEachJsonHub {
	
	public int num;
	public String str;
	public boolean bool;
	public Object nul;
	public List<String> list;
	
	protected float not_parse_because_not_public;
	public static long not_parse_because_static = -1L;
	
	public ForEachJsonHub() {
		
		num = 1;
		str = "STRING";
		bool = true;
		nul = null;
		list = Arrays.asList("a", "b", "c");
		
		not_parse_because_not_public = -1.0F;
	}

	public static void main(String[] args) {
		
		ForEachJsonHub pojo = new ForEachJsonHub();
		
		JsonHub jh = JsonHub.fromPojo(pojo);
		
		forEach(jh, "", "");
	}
	
	private static void forEach(JsonHub jh, String name, String indent) {
		
		JsonHubType type = jh.type();
		
		final String typeTag = "<" + type + "> ";
		final String nameTag = name.isEmpty() ? "" : "\"" + name + "\" is ";
		
		final String deepIndent = indent + "\t";
		
		switch ( type ) {
		case NULL: {
			
			echo(indent, typeTag, nameTag, jh.toString());
			break;
		}
		case TRUE:
		case FALSE: {
			
			echo(indent, typeTag, nameTag, jh.booleanValue());
			break;
		}
		case NUMBER: {
			
			echo(indent, typeTag, nameTag, jh.optionalNubmer().get());
			break;
		}
		case STRING: {
			
			echo(indent, typeTag, nameTag, jh.toString());
			break;
		}
		case ARRAY: {
			
			echo(indent, typeTag, nameTag, "[");
			
			jh.forEach((JsonString n, JsonHub v) -> {
				
				/*
				 * if type is ARRAY, JsonString is null
				 */
				
				forEach(v, "", deepIndent);
			});
			
			echo(indent, "]");
			break;
		}
		case OBJECT: {
			
			echo(indent, typeTag, nameTag, "{");
			
			jh.forEach((JsonString n, JsonHub v) -> {
				forEach(v, n.toString(), deepIndent);
			});
			
			echo(indent, "}");
			break;
		}
		}
	}
	
	private static void echo(Object... oo) {
		
		for ( Object o : oo ) {
			System.out.print(o);
		}
		
		System.out.println();
	}

}
