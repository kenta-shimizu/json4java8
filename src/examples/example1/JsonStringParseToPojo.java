package example1;

import java.util.List;

import jsonHub.JsonHub;

public class JsonStringParseToPojo {
	
	public int num;
	public String str;
	public boolean bool;
	public List<String> list;
	
	public JsonStringParseToPojo() {
		/* Nothing */
	}
	
	@Override
	public String toString() {
		
		return "{"
				+ "num: " + num
				+ ", str: \"" + str
				+ "\", bool: " + bool
				+ ", list.size(): "
				+ (list == null ? -1 : list.size())
				+ "}";
	}
	
	public static void main(String[] args) {
		
		String json = "{\"num\": 100, \"str\": \"string\", \"bool\": true, \"list\": [\"x\", \"y\", \"z\"]}";
		
		System.out.println("json: " + json);
		
		JsonStringParseToPojo pojo = JsonHub.fromJson(json).toPojo(JsonStringParseToPojo.class);
		
		System.out.println("pojo: " + pojo);
	}

}
