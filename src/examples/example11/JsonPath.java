package example11;

import java.util.List;

import com.shimizukenta.jsonhub.JsonHub;
import com.shimizukenta.jsonhub.JsonPathParseException;

public class JsonPath {

	public JsonPath() {
		/* Nothing */
	}
	
	public static void main(String[] args) {
		
		String json = "{ \"store\": {                            "
				+ "    \"book\": [                               "
				+ "     { \"category\": \"reference\",           "
				+ "        \"author\": \"Nigel Rees\",           "
				+ "        \"title\": \"Sayings of the Century\","
				+ "        \"price\": 8.95                       "
				+ "      },                                      "
				+ "      { \"category\": \"fiction\",            "
				+ "        \"author\": \"Evelyn Waugh\",         "
				+ "        \"title\": \"Sword of Honour\",       "
				+ "        \"price\": 12.99                      "
				+ "      },                                      "
				+ "      { \"category\": \"fiction\",            "
				+ "        \"author\": \"Herman Melville\",      "
				+ "        \"title\": \"Moby Dick\",             "
				+ "        \"isbn\": \"0-553-21311-3\",          "
				+ "        \"price\": 8.99                       "
				+ "      },                                      "
				+ "      { \"category\": \"fiction\",            "
				+ "        \"author\": \"J. R. R. Tolkien\",     "
				+ "        \"title\": \"The Lord of the Rings\", "
				+ "        \"isbn\": \"0-395-19395-8\",          "
				+ "        \"price\": 22.99                      "
				+ "      }                                       "
				+ "    ],                                        "
				+ "    \"bicycle\": {                            "
				+ "      \"color\": \"red\",                     "
				+ "      \"price\": 19.95                        "
				+ "    }                                         "
				+ "  }                                           "
				+ "}                                             ";
		
		JsonHub jh = JsonHub.fromJson(json);
		
		tryJsonPath(jh, "$.store.book[*].author");
		tryJsonPath(jh, "$..author");
		tryJsonPath(jh, "$.store.*");
		tryJsonPath(jh, "$.store..price");
		tryJsonPath(jh, "$..book[2]");
		tryJsonPath(jh, "$..book[-1]");
		tryJsonPath(jh, "$..book[:2]");
		
	}
	
	private static void tryJsonPath(JsonHub jh, String jsonPath) {
		try {
			System.out.println("JsonPath: \"" + jsonPath + "\"");
			List<JsonHub> ll = jh.jsonPath(jsonPath);
			System.out.println("result length: " + ll.size());
			ll.forEach(x -> {
				System.out.println(x.prettyPrint());
			});
			System.out.println();
		}
		catch (JsonPathParseException e) {
			e.printStackTrace();
		}
	}
}
