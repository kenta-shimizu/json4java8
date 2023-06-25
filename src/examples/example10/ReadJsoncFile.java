package example10;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import com.shimizukenta.jsonhub.JsonHub;
import com.shimizukenta.jsonhub.impl.JsoncReaderImpl;

public class ReadJsoncFile {

	public ReadJsoncFile() {
		/* Nothing */
	}

	public static void main(String[] args) {
		
		Path path = Paths.get("./ex10.jsonc");
		
		List<String> lines = Arrays.asList(
				"/*                         ",
				" * comments                ",
				" */                        ",
				"// comments                ",
				"{                          ",
				"  \"str\": \"STRING\",     ",
				"  \"num\": 100,            ",
				"  \"bool\": true,          ",
				"  \"array\": [             ",
				"    \"a\",                 ",
				"    \"b\",                 ",
				"    \"c\", //Trailing comma",
				"  ], //Trailing comma      ",
				"}                          "
				);
		
		try {
			
			if (! Files.exists(path)) {
				Files.write(path, lines,
						StandardOpenOption.WRITE,
						StandardOpenOption.CREATE);
			}
			
			JsonHub jh = JsoncReaderImpl.readFile(path);
			
			System.out.println(jh.prettyPrint());
		}
		catch ( Throwable t ) {
			t.printStackTrace();
		}
	}

}
