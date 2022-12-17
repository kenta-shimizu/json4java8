package com.shimizukenta.jsonhub;

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

/**
 * JSONC reader
 * 
 * <p>
 * Read JSONC and parse to JsonHub.<br />
 * </p>
 * 
 * @author kenta-shimizu
 *
 */
public final class JsoncReader {
	
	private JsoncReader() {
	}
	
	public static JsonHub fromLine(CharSequence line) {
		return fromLines(Collections.singletonList(line));
	}
	
	public static JsonHub fromLines(List<? extends CharSequence> lines) {
		return parse(lines);
	}
	
	public static JsonHub fromReader(Reader reader) throws IOException {
		try (
				BufferedReader br = new BufferedReader(reader);
				) {
			
			try (
					Stream<String> lines = br.lines();
					) {
				
				return fromLines(lines.collect(Collectors.toList()));
			}
		}
	}
	
	public static JsonHub fromFile(Path path) throws IOException {
		return fromLines(Files.readAllLines(path, StandardCharsets.UTF_8));
	}
	
	private static class JsoncLines {
		
		private final List<String> lines;
		private final int lineSize;
		
		private JsoncLines(List<? extends CharSequence> lines) {
			this.lines = lines.stream().map(CharSequence::toString).collect(Collectors.toList());
			this.lineSize = this.lines.size();
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
	
	//TODO
	private static final char CHAR_DQ = '"';
	private static final char CHAR_BS = '\\';
	private static final char CHAR_COMMA = ',';
	private static final char CHAR_ARRAY = ']';
	private static final char CHAR_OBJECT = '}';
	
	private static class SeekResult {
		
		private final SeekSymbol symbol;
		private final int pos;
		
		private SeekResult(SeekSymbol symbol, int pos) {
			this.symbol = symbol;
			this.pos = pos;
		}
	}
	
	private static SeekResult seekNextDoubleQuoteOrComment(JsoncLines jl, int line, int pos) {
		
		final String s = jl.getLine(line);
		final int m = s.length();
		
		for ( int p = pos; p < m; ++ p ) {
			
			final char c = s.charAt(p);
			
			if ( c == CHAR_DQ ) {
				
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
	
	private static SeekResult seekNextEndDoubleQuote(JsoncLines jl, int line, int pos) {
		
		final String s = jl.getLine(line);
		final int m = s.length();
		
		for ( int p = pos; p < m; ++ p ) {
			
			final char c = s.charAt(p);
			
			if ( c == CHAR_BS ) {
				
				++p;
				
			} else if ( c == CHAR_DQ ) {
				
				return new SeekResult(SeekSymbol.DQ, p);
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static SeekResult seekNextEndComment(JsoncLines jl, int line, int pos) {
		
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
			
			if ( c == CHAR_ARRAY ) {
				return new SeekResult(SeekSymbol.ARRAY, pos);
			} else if ( c == CHAR_OBJECT ) {
				return new SeekResult(SeekSymbol.OBJECT, pos);
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static SeekResult seekLastCommaOrOthers(CharSequence cs, int pos) {
		
		for (; pos >= 0; -- pos) {
			
			char c = cs.charAt(pos);
			
			if ( c == CHAR_COMMA ) {
				return new SeekResult(SeekSymbol.COMMA, pos);
			} else if ( c > CHAR_WS ) {
				return new SeekResult(SeekSymbol.OTHERS, pos);
			}
		}
		
		return new SeekResult(SeekSymbol.NOT_FOUND, -1);
	}
	
	private static JsonHub parse(List<? extends CharSequence> lines) {
		
		final StringBuilder sb = new StringBuilder();
		
		final JsoncLines jl = new JsoncLines(lines);
		
		/* remove comment */
		
		for (int pos = 0, line = 0; line < jl.lineSize; ) {
			
			SeekResult r = seekNextDoubleQuoteOrComment(jl, line, pos);
			
			switch ( r.symbol ) {
			case DQ: {
				
				SeekResult rr = seekNextEndDoubleQuote(jl, line, (r.pos + 1));
				
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
						
						if ( line >= jl.lineSize ) {
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
		
		return JsonHubJsonReader.getInstance().parse(sb);
	}
	
}
