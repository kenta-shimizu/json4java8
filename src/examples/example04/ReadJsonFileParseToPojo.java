package example04;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import jsonHub.JsonHub;

public class ReadJsonFileParseToPojo {
	
	public int num;
	public String str;
	public boolean bool;
	public Object nul;
	public List<String> list;
	
	protected float not_parse_because_not_public;
	public static long not_parse_because_static = -1L;
	
	public ReadJsonFileParseToPojo() {
		num = 1;
		str = "STRING";
		bool = true;
		nul = null;
		list = Arrays.asList("a", "b", "c");
		
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
		
		Path path = Paths.get("path_to_file.json");
		
		try {
			
			ReadJsonFileParseToPojo pojo = JsonHub.fromFile(path).toPojo(ReadJsonFileParseToPojo.class);
			
			System.out.println("pojo: " + pojo);
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		
		
		try (
				BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
				) {
			
			ReadJsonFileParseToPojo pojo = JsonHub.fromJson(br).toPojo(ReadJsonFileParseToPojo.class);
			
			System.out.println("pojo: " + pojo);
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
