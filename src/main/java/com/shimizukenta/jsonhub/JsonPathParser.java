package com.shimizukenta.jsonhub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is JsonPath parser.
 * 
 * @author kenta-shimizu
 *
 */
public final class JsonPathParser {

	private JsonPathParser() {
	}
	
	/**
	 * Returns List of match JsonHubs by JsonPath.
	 * 
	 * @param jh
	 * @param jsonPath
	 * @return List of match JsonHubs
	 * @throws JsonPathParseException
	 */
	public static List<JsonHub> parse(JsonHub jh, CharSequence jsonPath) {
		LinkedList<JsonPathGetter> ll = parseJsonPathList(jsonPath);
		return deepSeekPath(Stream.of(jh), ll).collect(Collectors.toList());
	}
	
	private static Stream<JsonHub> deepSeekPath(Stream<JsonHub> jhs, LinkedList<JsonPathGetter> ll) {
		if ( ll.isEmpty() ) {
			return jhs;
		} else {
			JsonPathGetter x = ll.removeFirst();
			return deepSeekPath(x.get(jhs), ll);
		}
	}
	
	private static interface JsonPathGetter {
		public Stream<JsonHub> get(Stream<JsonHub> jh);
	}
	
	private static class ChildObjectKeys implements JsonPathGetter {
		
		private final List<JsonString> keys;
		
		public ChildObjectKeys(List<JsonString> keys) {
			this.keys = Collections.unmodifiableList(keys);
		}
		
		@Override
		public Stream<JsonHub> get(Stream<JsonHub> jhs) {
			return jhs.flatMap(jh -> {
				if ( jh.isObject() ) {
					return this.keys.stream()
							.map(JsonString::unescaped)
							.flatMap(key -> {
								JsonHub a = jh.get(key);
								return a == null ? Stream.empty() : Stream.of(a);
							});
				}
				return Stream.empty();
			});
		}
	}
	
	private static class RecursiveObjectKeys implements JsonPathGetter {
		
		private final List<JsonString> keys;
		
		public RecursiveObjectKeys(List<JsonString> keys) {
			this.keys = Collections.unmodifiableList(keys);
		}
		
		@Override
		public Stream<JsonHub> get(Stream<JsonHub> jhs) {
			return jhs.flatMap(jh -> {
				final List<JsonHub> ll = new ArrayList<>();
				this.recursive(jh, ll);
				return ll.stream();
			});
		}
		
		private void recursive(JsonHub jh, List<JsonHub> ll) {
			if ( jh.isObject() ) {
				jh.forEach((js, v) -> {
					if ( this.keys.contains(js) ) {
						ll.add(v);
					}
					this.recursive(v, ll);
				});
			} else if ( jh.isArray() ) {
				jh.forEach(v -> {
					this.recursive(v, ll);
				});
			}
		}
	}
	
	private static class SliceArray {
		
		private final int start;
		private final int end;
		private final int step;
		
		public SliceArray(int start, int end, int step) {
			this.start = start;
			this.end = end;
			this.step = step;
		}
		
		public List<Integer> indices(int length) {
			
			final List<Integer> ii = new ArrayList<>();
			
			final int s = this.start < 0 ? (length + this.start) : this.start;
			final int e = this.end < 0 ? (length + this.end) : this.end;
			
			if ( this.step > 0 ) {
				
				for ( int i = s; i <= e; i += step ) {
					if ( i >= 0 && i < length ) {
						ii.add(Integer.valueOf(i));
					}
				}
				
			} else if ( this.step < 0 ) {
				
				for ( int i = s; i >= e; i += step ) {
					if ( i >= 0 && i < length ) {
						ii.add(Integer.valueOf(i));
					}
				}
			}
			
			return Collections.unmodifiableList(ii);
		}
	}
	
	private static class ChildArrayIndicies implements JsonPathGetter {
		
		private final List<SliceArray> slicies;
		
		public ChildArrayIndicies(List<SliceArray> slicies) {
			this.slicies = Collections.unmodifiableList(slicies);
		}
		
		@Override
		public Stream<JsonHub> get(Stream<JsonHub> jhs) {
			return jhs.flatMap(jh -> {
				if ( jh.isArray() ) {
					
					final List<Integer> idxs = new ArrayList<>();
					final int m = jh.length();
					
					for ( SliceArray sa : this.slicies ) {
						for (Integer i : sa.indices(m)) {
							if (! idxs.contains(i)) {
								idxs.add(i);
							}
						}
					}
					
					return idxs.stream().flatMap(i -> {
						return Stream.of(jh.get(i.intValue()));
					});
				}
				
				return Stream.empty();
			});
		}
	}
	
	private static class RecursiveArrayIndicies implements JsonPathGetter {
		
		private final List<SliceArray> slicies;
		
		public RecursiveArrayIndicies(List<SliceArray> slicies) {
			this.slicies = Collections.unmodifiableList(slicies);
		}
		
		@Override
		public Stream<JsonHub> get(Stream<JsonHub> jhs) {
			return jhs.flatMap(jh -> {
				final List<JsonHub> ll = new ArrayList<>();
				this.recursive(jh, ll);
				return ll.stream();
			});
		}
		
		private void recursive(JsonHub jh, List<JsonHub> ll) {
			if ( jh.isArray() ) {
				
				final List<Integer> idxs = new ArrayList<>();
				final int m = jh.length();
				
				for ( SliceArray sa : this.slicies ) {
					for (Integer i : sa.indices(m)) {
						if (! idxs.contains(i)) {
							idxs.add(i);
						}
					}
				}
				
				idxs.forEach(idx -> {
					ll.add(jh.get(idx));
				});
				
				jh.forEach(v -> {
					this.recursive(v, ll);
				});
				
			} else if ( jh.isObject() ) {
				
				jh.forEach(v -> {
					this.recursive(v, ll);
				});
			}
		}
	}
	
	private static class ChildWildcard implements JsonPathGetter {
		
		public ChildWildcard() {
		}
		
		@Override
		public Stream<JsonHub> get(Stream<JsonHub> jhs) {
			return jhs.flatMap(jh -> {
				if ( jh.isObject() || jh.isArray() ) {
					return jh.stream();
				}
				return Stream.empty();
			});
		}
	}
	
	private static class RecursiveWildcard implements JsonPathGetter {
		
		public RecursiveWildcard() {
		}
		
		@Override
		public Stream<JsonHub> get(Stream<JsonHub> jhs) {
			return jhs.flatMap(jh -> {
				List<JsonHub> ll = new ArrayList<>();
				this.recursive(jh, ll);
				return ll.stream();
			});
		}
		
		private void recursive(JsonHub jh, List<JsonHub> ll) {
			if ( jh.isObject() || jh.isArray() ) {
				for ( JsonHub a : jh ) {
					ll.add(a);
					this.recursive(a, ll);
				}
			}
		}
	}
	
	//TODO
	//filter
	
	
	private static LinkedList<JsonPathGetter> parseJsonPathList(CharSequence jsonpath) {
		
		final LinkedList<JsonPathGetter> ll = new LinkedList<>();
		
		String s = jsonpath.toString().trim();
		
		if ( ! s.startsWith("$") ) {
			throw new JsonPathParseException("Not startwith \"$\".");
		}
		
		deepParse(s, 1, ll);
		return ll;
	}
	
	private static final JsonPathGetter childWildcard = new ChildWildcard();
	private static final JsonPathGetter recursiveWildcard = new RecursiveWildcard();
	
	private static void deepParse(String jp, int pos, LinkedList<JsonPathGetter> ll) {
		
		final int len = jp.length();
		
		if (pos >= len) {
			return;
		}
		
		if (jp.startsWith("...", pos)) {
			throw new JsonPathParseException("Not oarse \"...\", position: " + pos);
		}
		
		if (jp.startsWith("..[", pos)) {
			FindBracketGetterResult r = findBracketEnd(jp, pos + 3, true);
			ll.add(r.getter);
			deepParse(jp, r.pos, ll);
			return;
		}
		
		if (jp.startsWith("..*", pos)) {
			ll.add(recursiveWildcard);
			deepParse(jp, pos + 3, ll);
			return;
		}
		
		if (jp.startsWith(".*", pos)) {
			ll.add(childWildcard);
			deepParse(jp, pos + 2, ll);
			return;
		}
		
		if (jp.startsWith("..", pos)) {
			
			final int nextPos = findNextPeriodOrBracketOrEnd(jp, pos + 2);
			
			if (nextPos <= (pos + 2)) {
				throw new JsonPathParseException("Not found key. position: " + pos);
			}
			
			final String key = jp.substring(pos + 2, nextPos);
			ll.add(new RecursiveObjectKeys(Collections.singletonList(JsonString.escaped(key))));
			deepParse(jp, nextPos, ll);
			return;
		}
		
		if (jp.startsWith(".[", pos)) {
			FindBracketGetterResult r = findBracketEnd(jp, pos + 2, false);
			ll.add(r.getter);
			deepParse(jp, r.pos, ll);
			return;
		}
		
		if (jp.charAt(pos) == '.') {
			
			final int nextPos = findNextPeriodOrBracketOrEnd(jp, pos + 1);
			
			if (nextPos <= (pos + 1)) {
				throw new JsonPathParseException("Not found key. position: " + pos);
			}
			
			final String key = jp.substring(pos + 1, nextPos);
			ll.add(new ChildObjectKeys(Collections.singletonList(JsonString.escaped(key))));
			deepParse(jp, nextPos, ll);
			return;
		}
		
		if (jp.charAt(pos) == '[') {
			FindBracketGetterResult r = findBracketEnd(jp, pos + 1, false);
			ll.add(r.getter);
			deepParse(jp, r.pos, ll);
			return;
		}
		
		throw new JsonPathParseException("Unkonwn format. position: " + pos);
	}
	
	private static int findNextPeriodOrBracketOrEnd(String jp, int pos) {
		FindCharResult r = FindChars.nextIgnoreEscape(jp, pos, '.', '[');
		if ( r.pos < 0 ) {
			return jp.length();
		} else {
			return r.pos;
		}
	}
	
	private static class FindBracketGetterResult {
		
		private final JsonPathGetter getter;
		private final int pos;
		
		public FindBracketGetterResult(JsonPathGetter getter, int pos) {
			this.getter = getter;
			this.pos = pos;
		}
	}
	
	private static FindBracketGetterResult findBracketEnd(String jp, int pos, boolean recursive) {
		
		final FindCharResult r = FindChars.nextIgnoreWhiteSpace(jp, pos);
		
		if (
				(r.c >= '0' && r.c <= '9')
				|| r.c == '-'
				|| r.c == ':'
				|| r.c == '+'
				) {
			
			final FindCharResult er = FindChars.next(jp, r.pos + 1, ']');
			
			if ( er.pos < 0 ) {
				throw new JsonPathParseException("Bracket not understand. position: " + pos);
			}
			
			try {
				final List<SliceArray> nll = new ArrayList<>();
				String[] ss = jp.substring(r.pos, er.pos).split(",");
				
				for ( String n : ss ) {
					String[] nn = n.split(":", 3);
				
					if ( nn.length == 1 ) {
						
						int i0 = Integer.parseInt(nn[0].trim(), 10);
						nll.add(new SliceArray(i0, i0, 1));
						
					} else if ( nn.length == 2 ) {
						
						final int i0;
						String nn0 = nn[0].trim();
						if ( nn0.isEmpty() ) {
							i0 = 0;
						} else {
							i0 = Integer.parseInt(nn0, 10);
						}
						
						final int i1;
						String nn1 = nn[1].trim();
						if ( nn1.isEmpty() ) {
							i1 = -1;
						} else {
							i1 = Integer.parseInt(nn1, 10);
						}
						
						nll.add(new SliceArray(i0, i1, 1));
						
					} else {
						
						final int i0;
						String nn0 = nn[0].trim();
						if ( nn0.isEmpty() ) {
							i0 = 0;
						} else {
							i0 = Integer.parseInt(nn0, 10);
						}
						
						final int i1;
						String nn1 = nn[1].trim();
						if ( nn1.isEmpty() ) {
							i1 = -1;
						} else {
							i1 = Integer.parseInt(nn1, 10);
						}
						
						final int i2;
						String nn2 = nn[2].trim();
						if ( nn2.isEmpty() ) {
							i2 = 1;
						} else {
							i2 = Integer.parseInt(nn2, 10);
						}
						
						nll.add(new SliceArray(i0, i1, i2));
					}
				}
				
				if ( recursive ) {
					return new FindBracketGetterResult(new RecursiveArrayIndicies(nll), er.pos + 1);
				} else {
					return new FindBracketGetterResult(new ChildArrayIndicies(nll), er.pos + 1);
				}
			}
			catch ( NumberFormatException e ) {
				throw new JsonPathParseException("NumberFormatException. position: " + pos, e);
			}
		}
		
		if ( r.c == '\'' || r.c == '"' ) {
			final List<JsonString> ll = new ArrayList<>();
			int p = deepFindObjectKeys(jp, r.pos, ll);
			if ( recursive ) {
				return new FindBracketGetterResult(new RecursiveObjectKeys(ll), p);
			} else {
				return new FindBracketGetterResult(new ChildObjectKeys(ll), p);
			}
		}
		
		if ( r.c == '?' ) {
			
			//TODO
			//filter
			
			throw new JsonPathParseException("Not yet supported");
		}
		
		if (r.c == '(') {
			
			//TODO
			//script
			
			throw new JsonPathParseException("Not yet supported");
		}
		
		throw new JsonPathParseException("Bracket not understand. position: " + pos);
	}
	
	private static int deepFindObjectKeys(String jp, int fromIndex, List<JsonString> ll) {
		
		final FindCharResult sr = FindChars.next(jp, fromIndex, '"', '\'');
		
		if (sr.c == '"' || sr.c == '\'') {
			
			final FindCharResult er;
			
			if (sr.c == '"') {
				
				er = FindChars.nextIgnoreEscape(jp, sr.pos + 1, '"');
				
				if ( er.pos < 0 ) {
					throw new JsonPathParseException("Not found end \", position: " + sr.pos);
				}
				
			} else {
				
				er = FindChars.nextIgnoreEscape(jp, sr.pos + 1, '\'');
				
				if ( er.pos < 0 ) {
					throw new JsonPathParseException("Not found end ', position: " + sr.pos);
				}
			}
			
			ll.add(JsonString.escaped(jp.substring(sr.pos + 1, er.pos)));
			
			final FindCharResult xr = FindChars.next(jp, er.pos + 1, ',', ']');
			
			if (xr.c == ']') {
				return xr.pos + 1;
			} else if (xr.c == ',') {
				return deepFindObjectKeys(jp, xr.pos + 1, ll);
			} else {
				throw new JsonPathParseException("Not found end bracket, position: " + (xr.pos + 1));
			}
			
		} else {
			
			throw new JsonPathParseException("Not found start QUOT, position: " + fromIndex);
		}
	}
	
}
