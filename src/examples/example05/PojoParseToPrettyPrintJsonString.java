package example05;

import java.util.Arrays;
import java.util.List;

import jsonHub.JsonHub;

public class PojoParseToPrettyPrintJsonString {
	
	public int num;
	public String str;
	public boolean bool;
	public Object nul;
	public List<String> list;
	
	protected float not_parse_because_not_public;
	public static long not_parse_because_static = -1L;
	
	public PojoParseToPrettyPrintJsonString() {
		
		num = 1;
		str = "STRING";
		bool = true;
		nul = null;
		list = Arrays.asList("a", "b", "c");
		
		not_parse_because_not_public = -1.0F;
	}

	public static void main(String[] args) {
		
		PojoParseToPrettyPrintJsonString pojo = new PojoParseToPrettyPrintJsonString();
		
		String prettyPrintJson = JsonHub.fromPojo(pojo).prettyPrint();
		
		System.out.println(prettyPrintJson);
	}

}
