package example01;

import java.util.List;

import jsonHub.JsonHub;

public class JsonStringParseToPojo {
	
	public int num;
	public String str;
	public boolean bool;
	public Object nul;
	public List<String> list;
	
	protected float not_parse_because_not_public;
	public static long not_parse_because_static = -1L;
	
	public JsonStringParseToPojo() {
		
		not_parse_because_not_public = -1.0F;
	}
	
	@Override
	public String toString() {
		
		return "{"
				+ "num: " + num
				+ ", str: \"" + str
				+ "\", bool: " + bool
				+ ", nul: " + nul
				+ ", list.size(): "
				+ (list == null ? -1 : list.size())
				+ "}";
	}
	
	public static void main(String[] args) {
		
		String json = "{\"num\": 100, \"str\": \"string\", \"bool\": true, \"nul\": null, \"list\": [\"a\", \"b\", \"c\"]}";
		
		System.out.println("json: " + json);
		
		JsonStringParseToPojo pojo = JsonHub.fromJson(json).toPojo(JsonStringParseToPojo.class);
		
		System.out.println("pojo: " + pojo);
	}

}
