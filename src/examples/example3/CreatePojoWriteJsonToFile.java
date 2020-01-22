package example3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import jsonHub.JsonHub;

public class CreatePojoWriteJsonToFile {
	
	public int num;
	public String str;
	public boolean bool;
	public List<String> list;
	
	protected float not_parse_because_not_public;
	public static long not_parse_because_static = -1L;
	
	public CreatePojoWriteJsonToFile() {
		
		num = 1;
		str = "STRING";
		bool = true;
		list = Arrays.asList("a", "b", "c");
		
		not_parse_because_not_public = -1.0F;
	}

	public static void main(String[] args) {
		
		Path path = Paths.get("path_to_file.json");
		
		CreatePojoWriteJsonToFile pojo = new CreatePojoWriteJsonToFile();
		
		try (
				BufferedWriter bw = Files.newBufferedWriter(path);
				) {
			
			JsonHub.fromPojo(pojo).toJson(bw);
			
			System.out.println("wrote to " + path.toAbsolutePath());
		}
		catch ( IOException e ) {
			e.printStackTrace();
			
			System.out.println("write failed");
		}
		
	}

}
