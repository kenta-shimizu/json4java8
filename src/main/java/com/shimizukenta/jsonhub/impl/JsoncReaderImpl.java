package com.shimizukenta.jsonhub.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.shimizukenta.jsonhub.JsonHub;
import com.shimizukenta.jsonhub.JsonHubParseException;
import com.shimizukenta.jsonhub.JsoncReaderException;

/**
 * JSONC reader to JsonHub.
 * 
 * <p>
 * Read JSONC and parse to JsonHub.<br />
 * </p>
 * <ul>
 * <li>Comments. (// and /* ...)</li>
 * <li>Trailing Comma(,) in Array and Object.</li>
 * </ul>
 * 
 * @author kenta-shimizu
 *
 */
public final class JsoncReaderImpl {
	
	private JsoncReaderImpl() {
	}
	
	/**
	 * Returns parsed JsonHub instance from JSONC String.
	 * 
	 * @param line of JSONC String.
	 * @return parsed JsonHub.
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readLine(CharSequence line) {
		return readLines(Collections.singletonList(line));
	}
	
	/**
	 * Returns parsed JsonHub instance from JSONC lines.
	 * 
	 * @param lines of JSONC String 
	 * @return parsed JsonHub.
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readLines(List<? extends CharSequence> lines) {
		return parse(lines);
	}
	
	/**
	 * Returns parsed JsonHub instance from JSONC reader.
	 * 
	 * @param reader of JSONC
	 * @return parsed JsonHub.
	 * @throws IOException if IO-Exception
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readReader(Reader reader) throws IOException {
		try (
				BufferedReader br = new BufferedReader(reader);
				) {
			
			try (
					Stream<String> lines = br.lines();
					) {
				
				return readLines(lines.collect(Collectors.toList()));
			}
		}
	}
	
	/**
	 * Returns parsed JsonHub instance from JSONC file.
	 * 
	 * @param path of JSONC file
	 * @return parsed JsonHub.
	 * @throws IOException if IO-Exception
	 * @throws JsonHubParseException if parse failed
	 * @throws JsoncReaderException if parse failed
	 */
	public static JsonHub readFile(Path path) throws IOException {
		return readLines(Files.readAllLines(path, StandardCharsets.UTF_8));
	}
	
	private static class JsoncLinePack {
		
		private final List<String> lines;
		private final int size;
		
		private JsoncLinePack(List<? extends CharSequence> lines) {
			this.lines = lines.stream().map(CharSequence::toString).collect(Collectors.toList());
			this.size = this.lines.size();
		}
		
		private String getLine(int line) {
			return this.lines.get(line);
		}
	}
	
	private static enum SeekSymbol {
		
		NOT_FOUND,
		
		DQ,	/* '"' */
		SA,	/* '/'&'*' */
		AS, /* '*'&'/' */
		SS, /* '/'&'/' */
		
		OTHERS,
		COMMA, /* , */
		ARRAY, /* ] */
		OBJECT, /* } */
		;
	}
	
	private static final char CHAR_SLASH = '/';
	private static final char CHAR_APO = '*';
	private static final char CHAR_WS = ' ';
	
	private static class SeekResult {
		
		private final SeekSymbol symbol;
		private final int pos;
		
		private SeekResult(SeekSymbol symbol, int pos) {
			this.symbol = symbol;
			this.pos = pos;
		}
	}
	
	private static SeekResult seekNextQuotOrComment(JsoncLinePack jl, int line, int pos) {
		
		final String s = jl.getLine(line);
		final int m = s.length();
		
		for ( int p = pos; p < m; ++ p ) {
			
			final char c = s.charAt(p);
			
			if (JsonStructuralChar.QUOT.match(c)) {
				
				return new SeekResult(SeekSymbol.DQ, p);
				
			} else if ( c == CHAR_SLASH ) {
				
				if ( p < (m - 1) ) {
					
					final char nc = s.charAt(p + 1);
					
					if ( nc == CHAR_APO ) {
						
						return new SeekResult(SeekSymbol.SA, p);
						
					} else if ( nc == CHAR_SLASH ) {
						
						return new SeekResult(SeekSymbol.SS, p);
					}
				}
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static SeekResult seekNextEndQuot(JsoncLinePack jl, int line, int pos) {
		
		final String s = jl.getLine(line);
		final int m = s.length();
		
		for ( int p = pos; p < m; ++ p ) {
			
			final char c = s.charAt(p);
			
			if (JsonStructuralChar.ESCAPE.match(c)) {
				
				++p;
				
			} else if (JsonStructuralChar.QUOT.match(c)) {
				
				return new SeekResult(SeekSymbol.DQ, p);
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static SeekResult seekNextEndComment(JsoncLinePack jl, int line, int pos) {
		
		final String s = jl.getLine(line);
		final int m = s.length();
		
		for ( int p = pos; p < (m - 1); ++ p ) {
			
			char c = s.charAt(p);
			
			if ( c == CHAR_APO ) {
				
				if ( s.charAt(p + 1) == CHAR_SLASH ) {
					return new SeekResult(SeekSymbol.AS, p);
				}
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static SeekResult seekLastArrayOrObject(CharSequence cs, int pos) {
		
		for (; pos >= 0; -- pos) {
			
			char c = cs.charAt(pos);
			
			if (JsonStructuralChar.ARRAY_END.match(c)) {
				return new SeekResult(SeekSymbol.ARRAY, pos);
			} else if (JsonStructuralChar.OBJECT_END.match(c)) {
				return new SeekResult(SeekSymbol.OBJECT, pos);
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static SeekResult seekLastCommaOrOthers(CharSequence cs, int pos) {
		
		for (; pos >= 0; -- pos) {
			
			char c = cs.charAt(pos);
			
			if (JsonStructuralChar.SEPARATOR_VALUE.match(c)) {
				return new SeekResult(SeekSymbol.COMMA, pos);
			} else if ( c > CHAR_WS ) {
				return new SeekResult(SeekSymbol.OTHERS, pos);
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static JsonHub parse(List<? extends CharSequence> lines) {
		
		final StringBuilder sb = new StringBuilder();
		
		final JsoncLinePack jl = new JsoncLinePack(lines);
		
		/* remove comment */
		
		for (int pos = 0, line = 0; line < jl.size; ) {
			
			SeekResult r = seekNextQuotOrComment(jl, line, pos);
			
			switch ( r.symbol ) {
			case DQ: {
				
				SeekResult rr = seekNextEndQuot(jl, line, (r.pos + 1));
				
				if ( rr.symbol == SeekSymbol.DQ ) {
					
					sb.append(jl.getLine(line).substring(pos, (rr.pos + 1)));
					pos = rr.pos + 1;
					
				} else {
					
					throw new JsoncReaderException("Not found end of \", line: " + (line + 1) + ",pos: " + pos);
				}
				
				break;
			}
			case SA: {
				
				sb.append(jl.getLine(line).substring(pos, r.pos));
				
				final int expline = line + 1;
				final int exppos = r.pos;
				
				pos = r.pos + 2;
				
				for ( ;; ) {
					SeekResult rr = seekNextEndComment(jl, line, pos);
					
					if (rr.symbol == SeekSymbol.AS) {
						
						pos = rr.pos + 2;
						break;
						
					} else {
						
						++ line;
						pos = 0;
						
						if ( line >= jl.size ) {
							throw new JsoncReaderException("Not found end of /*, line: " + expline + ", pos: " + exppos);
						}
					}
				}
				
				break;
			}
			case SS: {
				sb.append(jl.getLine(line).substring(pos, r.pos));
				++ line;
				pos = 0;
				break;
			}
			case NOT_FOUND:
			default: {
				sb.append(jl.getLine(line).substring(pos));
				++ line;
				pos = 0;
			}
			}
		}
		
		/* remove trailing comma */
		
		for ( int pos = sb.length() - 1; pos >= 0; ) {
			
			SeekResult r = seekLastArrayOrObject(sb, pos);
			
			switch (r.symbol) {
			case ARRAY:
			case OBJECT: {
				
				pos = r.pos - 1;
				
				SeekResult rr = seekLastCommaOrOthers(sb, pos);
				
				switch (rr.symbol) {
				case COMMA: {
					sb.deleteCharAt(rr.pos);
					pos = rr.pos - 1;
					break;
				}
				case OTHERS: {
					pos = rr.pos;
					break;
				}
				case NOT_FOUND:
				default: {
					pos = -1;
				}
				}
				
				break;
			}
			case NOT_FOUND:
			default: {
				pos = -1;
			}
			}
		}
		
		return JsonReader.fromJson(sb);
	}
	
}
