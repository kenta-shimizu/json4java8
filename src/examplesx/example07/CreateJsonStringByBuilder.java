package example07;

import jsonHub.JsonHub;
import jsonHub.JsonHubBuilder;

public class CreateJsonStringByBuilder {

	public CreateJsonStringByBuilder() {
		/* Nothign */
	}
	
	public static void main(String[] args) {
		
		JsonHubBuilder bb = JsonHub.getBuilder();
		
		JsonHub jh = bb.object(
				bb.pair("num", 100),
				bb.pair("str", "STRING"),
				bb.pair("bool", true),
				bb.pair("nul", null),
				bb.pair("list", bb.array(
						bb.build("a"),
						bb.build("b"),
						bb.build("c")
						))
				);
		
		System.out.println(jh.toJson());
	}

}
