package jsonValue;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonValueBuilder {

	private JsonValueBuilder() {
		/* Nothing */
	}
	
	private static class SingletonHolder {
		private static final JsonValueBuilder inst = new JsonValueBuilder();
		private static final JsonNullValue nullValue = new JsonNullValue();
		private static final JsonTrueValue trueValue = new JsonTrueValue();
		private static final JsonFalseValue falseValue = new JsonFalseValue();
	}
	
	public static JsonValueBuilder getInstance() {
		return SingletonHolder.inst;
	}
	
	/**
	 * 
	 * @return JsonNullValue
	 */
	public JsonNullValue nullValue() {
		return SingletonHolder.nullValue;
	}
	
	/**
	 * 
	 * @return JsonTrueValue
	 */
	public JsonTrueValue trueValue() {
		return SingletonHolder.trueValue;
	}
	
	/**
	 * 
	 * @return JsonFalseValue
	 */
	public JsonFalseValue falseValue() {
		return SingletonHolder.falseValue;
	}
	
	public JsonNumberValue build(int v) {
		return new JsonNumberValue(String.valueOf(v));
	}
	
	public JsonNumberValue build(long v) {
		return new JsonNumberValue(String.valueOf(v));
	}
	
	public JsonNumberValue build(float v) {
		return new JsonNumberValue(String.valueOf(v));
	}
	
	public JsonNumberValue build(double v) {
		return new JsonNumberValue(String.valueOf(v));
	}
	
	public JsonValue build(boolean v) {
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
	public JsonValue build(Object v) {
		
		if ( v == null ) {
			
			return nullValue();
			
		} else {
			
			if ( v instanceof Boolean ) {
				build(((Boolean) v).booleanValue());
			}
			
			if ( v instanceof CharSequence ) {
				return new JsonStringValue(JsonString.unescaped((CharSequence)v));
			}
			
			if ( v instanceof JsonString ) {
				return new JsonStringValue((JsonString)v);
			}
			
			if ( v instanceof Number ) {
				return new JsonNumberValue(((Number)v).toString());
			}
			
			throw new JsonValueBuildException("build failed \"" + v.toString() + "\"");
		}
	}
	
	
	public JsonArrayValue array() {
		return new JsonArrayValue(Collections.emptyList());
	}
	
	public JsonArrayValue array(JsonValue... v) {
		return new JsonArrayValue(Arrays.asList(v));
	}
	
	public JsonValue array(List<? extends JsonValue> v) {
		
		if ( v == null ) {
			
			return nullValue();
			
		} else {
			
			return new JsonArrayValue(v);
		}
	}
	
	
	/**
	 * 
	 * @return blank OBJECT
	 */
	public JsonObjectValue object() {
		return new JsonObjectValue(Collections.emptyList());
	}
	
	/**
	 * 
	 * @param v
	 * @return OBJECT
	 */
	public JsonObjectValue object(JsonObjectPair... v) {
		return new JsonObjectValue(Arrays.asList(v));
	}

	/**
	 * 
	 * @param v
	 * @return OBJECT or NULL
	 */
	public JsonValue object(Collection<? extends JsonObjectPair> v) {
		
		if ( v == null ) {
			
			return nullValue();
			
		} else {
			
			return new JsonObjectValue(v);
		}
	}
	
	/**
	 * 
	 * @param v
	 * @return OBJECT or NULL
	 */
	public JsonValue object(Map<? extends JsonString, ? extends JsonValue> v) {
		
		if ( v == null ) {
			
			return nullValue();
			
		} else {
			
			final Collection<JsonObjectPair> pairs = new ArrayList<>();
			
			v.forEach((name, value) -> {
				pairs.add(pair(name, value));
			});
			
			return object(pairs);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 * @throws JsonValueBuildException
	 */
	public JsonObjectPair pair(JsonString name, Object v) {
		if ((v != null) && (v instanceof JsonValue)) {
			return new JsonObjectPair(name, (JsonValue)v);
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
	 * @throws JsonValueBuildException
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
	 * @return JsonValue
	 * @throws JsonValueParseException
	 */
	public JsonValue fromJson(CharSequence v) {
		return JsonValueParser.getInstance().parse(v);
	}
	
	/**
	 * 
	 * @param reader
	 * @return JsonValue
	 * @throws IOException
	 * @throws JsonValueParseException
	 */
	public JsonValue fromJson(Reader reader) throws IOException {
		return JsonValueParser.getInstance().parse(reader);
	}
	
}
