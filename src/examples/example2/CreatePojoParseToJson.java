package example2;

import java.util.Arrays;
import java.util.List;

import jsonHub.JsonHub;

public class CreatePojoParseToJson {
	
	public int num;
	public String str;
	public boolean bool;
	public List<String> list;
	
	protected float not_parse_because_not_public;
	public static long not_parse_because_static = -1L;
	
	public CreatePojoParseToJson() {
		
		num = 1;
		str = "STRING";
		bool = true;
		list = Arrays.asList("a", "b", "c");
		
		not_parse_because_not_public = -1.0F;
	}

	public static void main(String[] args) {
		
		CreatePojoParseToJson pojo = new CreatePojoParseToJson();
		
		String json = JsonHub.fromPojo(pojo).toJson();
		
		System.out.println("json: " + json);
	}

}
