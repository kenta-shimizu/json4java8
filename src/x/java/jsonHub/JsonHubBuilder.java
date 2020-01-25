package jsonHub;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonHubBuilder {

	private JsonHubBuilder() {
		/* Nothing */
	}
	
	private static class SingletonHolder {
		private static final JsonHubBuilder inst = new JsonHubBuilder();
		
		private static final JsonString emptyString = JsonString.escaped("");
		private static final JsonNullHub nullValue = new JsonNullHub();
		private static final JsonTrueHub trueValue = new JsonTrueHub();
		private static final JsonFalseHub falseValue = new JsonFalseHub();
		private static final JsonArrayHub emptyArrayValue = new JsonArrayHub(Collections.emptyList());
		private static final JsonObjectHub emptyObjectValue = new JsonObjectHub(Collections.emptyList());
		private static final JsonStringHub emptyStringValue = new JsonStringHub(emptyString);
	}
	
	public static JsonHubBuilder getInstance() {
		return SingletonHolder.inst;
	}
	
	/**
	 * 
	 * @return JsonNullHub
	 */
	public JsonNullHub nullValue() {
		return SingletonHolder.nullValue;
	}
	
	/**
	 * 
	 * @return JsonTrueHub
	 */
	public JsonTrueHub trueValue() {
		return SingletonHolder.trueValue;
	}
	
	/**
	 * 
	 * @return JsonFalseHub
	 */
	public JsonFalseHub falseValue() {
		return SingletonHolder.falseValue;
	}
	
	public JsonNumberHub build(int v) {
		return number(v);
	}
	
	public JsonNumberHub build(long v) {
		return number(v);
	}
	
	public JsonNumberHub build(float v) {
		return number(v);
	}
	
	public JsonNumberHub build(double v) {
		return number(v);
	}
	
	public JsonHub build(boolean v) {
		if ( v ) {
			return trueValue();
		} else {
			return falseValue();
		}
	}
	
	/**
	 * 
	 * @param v
	 * @return NULL, TRUE, FALSE, STRING, NUMBER
	 */
	public JsonHub build(Object v) {
		
		if ( v == null ) {
			
			return nullValue();
			
		} else {
			
			if ( v instanceof Boolean ) {
				build(((Boolean) v).booleanValue());
			}
			
			if ( v instanceof CharSequence ) {
				return string((CharSequence)v);
			}
			
			if ( v instanceof JsonString ) {
				return string((JsonString)v);
			}
			
			if ( v instanceof Number ) {
				return number((Number)v);
			}
			
			throw new JsonHubBuildException("build failed \"" + v.toString() + "\"");
		}
	}
	
	
	/**
	 * 
	 * @param cs
	 * @return JsonNumberHub
	 */
	public JsonNumberHub number(CharSequence cs) {
		return new JsonNumberHub(cs);
	}
	
	public JsonNumberHub number(Number n) {
		return new JsonNumberHub(n);
	}
	
	public JsonNumberHub number(int n) {
		return number(Integer.valueOf(n));
	}
	
	public JsonNumberHub number(long n) {
		return number(Long.valueOf(n));
	}
	
	public JsonNumberHub number(float n) {
		return number(Float.valueOf(n));
	}
	
	public JsonNumberHub number(double n) {
		return number(Double.valueOf(n));
	}
	
	
	/**
	 * 
	 * @param v
	 * @return STRING
	 */
	public JsonStringHub string(CharSequence v) {
		if ( Objects.requireNonNull(v).toString().isEmpty() ) {
			return SingletonHolder.emptyStringValue;
		} else {
			return string(JsonString.unescaped(v));
		}
	}
	
	/**
	 * 
	 * @param v
	 * @return STRING
	 */
	public JsonStringHub string(JsonString v) {
		return new JsonStringHub(v);
	}
	
	/**
	 * 
	 * @return empty ARRAY
	 */
	public JsonArrayHub array() {
		return emptyArray();
	}
	
	/**
	 * 
	 * @param v
	 * @return ARRAY
	 */
	public JsonArrayHub array(JsonHub... v) {
		return new JsonArrayHub(Arrays.asList(v));
	}
	
	/**
	 * 
	 * @param v
	 * @return ARRAY
	 */
	public JsonArrayHub array(List<? extends JsonHub> v) {
		if ( v.isEmpty() ) {
			return emptyArray();
		} else {
			return new JsonArrayHub(v);
		}
	}
	
	public JsonArrayHub emptyArray() {
		return SingletonHolder.emptyArrayValue;
	}
	
	
	/**
	 * 
	 * @return empty OBJECT
	 */
	public JsonObjectHub object() {
		return emptyObject();
	}
	
	/**
	 * 
	 * @param v
	 * @return OBJECT
	 */
	public JsonObjectHub object(JsonObjectPair... v) {
		return object(Arrays.asList(v));
	}

	/**
	 * 
	 * @param v
	 * @return OBJECT
	 */
	public JsonObjectHub object(Collection<? extends JsonObjectPair> v) {
		if ( Objects.requireNonNull(v).isEmpty() ) {
			return emptyObject();
		} else {
			return new JsonObjectHub(v);
		}
	}
	
	/**
	 * 
	 * @param v
	 * @return OBJECT
	 */
	public JsonObjectHub object(Map<? extends JsonString, ? extends JsonHub> v) {
		
		final Collection<JsonObjectPair> pairs = new ArrayList<>();
		
		Objects.requireNonNull(v).forEach((name, value) -> {
			pairs.add(pair(name, value));
		});
			
		return object(pairs);
	}
	
	public JsonObjectHub emptyObject() {
		return SingletonHolder.emptyObjectValue;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 * @throws JsonHubBuildException
	 */
	public JsonObjectPair pair(JsonString name, Object v) {
		if ((v != null) && (v instanceof JsonHub)) {
			return new JsonObjectPair(name, (JsonHub)v);
		} else {
			return new JsonObjectPair(name, build(v));
		}
	}
	
	public JsonObjectPair pair(JsonString name, int v) {
		return new JsonObjectPair(name, build(v));
	}
	
	public JsonObjectPair pair(JsonString name, long v) {
		return new JsonObjectPair(name, build(v));
	}
	
	public JsonObjectPair pair(JsonString name, float v) {
		return new JsonObjectPair(name, build(v));
	}
	
	public JsonObjectPair pair(JsonString name, double v) {
		return new JsonObjectPair(name, build(v));
	}
	
	public JsonObjectPair pair(JsonString name, boolean v) {
		return new JsonObjectPair(name, build(v));
	}
	
	/**
	 * 
	 * @param name
	 * @param v
	 * @return JsonObjectPair
	 * @throws JsonHubBuildException
	 */
	public JsonObjectPair pair(CharSequence name, Object v) {
		return pair(JsonString.unescaped(name), v);
	}
	
	public JsonObjectPair pair(CharSequence name, int v) {
		return pair(JsonString.unescaped(name), v);
	}
	
	public JsonObjectPair pair(CharSequence name, long v) {
		return pair(JsonString.unescaped(name), v);
	}
	
	public JsonObjectPair pair(CharSequence name, float v) {
		return pair(JsonString.unescaped(name), v);
	}
	
	public JsonObjectPair pair(CharSequence name, double v) {
		return pair(JsonString.unescaped(name), v);
	}
	
	public JsonObjectPair pair(CharSequence name, boolean v) {
		return pair(JsonString.unescaped(name), v);
	}
	
	
	/**
	 * 
	 * @param json
	 * @return JsonHub
	 * @throws JsonHubParseException
	 */
	public JsonHub fromJson(CharSequence v) {
		return JsonHubJsonParser.getInstance().parse(v);
	}
	
	/**
	 * 
	 * @param reader
	 * @return JsonHub
	 * @throws IOException
	 * @throws JsonHubParseException
	 */
	public JsonHub fromJson(Reader reader) throws IOException {
		return JsonHubJsonParser.getInstance().parse(reader);
	}
	
}
