package jsonValue;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonValueBuilder {

	private JsonValueBuilder() {
		/* Nothing */
	}
	
	private static class SingletonHolder {
		private static final JsonValueBuilder inst = new JsonValueBuilder();
		
		private static final JsonString emptyString = JsonString.escaped("");
		private static final JsonNullValue nullValue = new JsonNullValue();
		private static final JsonTrueValue trueValue = new JsonTrueValue();
		private static final JsonFalseValue falseValue = new JsonFalseValue();
		private static final JsonArrayValue emptyArrayValue = new JsonArrayValue(Collections.emptyList());
		private static final JsonObjectValue emptyObjectValue = new JsonObjectValue(Collections.emptyList());
		private static final JsonStringValue emptyStringValue = new JsonStringValue(emptyString);
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
		return number(v);
	}
	
	public JsonNumberValue build(long v) {
		return number(v);
	}
	
	public JsonNumberValue build(float v) {
		return number(v);
	}
	
	public JsonNumberValue build(double v) {
		return number(v);
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
				return string((CharSequence)v);
			}
			
			if ( v instanceof JsonString ) {
				return string((JsonString)v);
			}
			
			if ( v instanceof Number ) {
				return number((Number)v);
			}
			
			throw new JsonValueBuildException("build failed \"" + v.toString() + "\"");
		}
	}
	
	
	/**
	 * 
	 * @param cs
	 * @return JsonNumberValue
	 */
	public JsonNumberValue number(CharSequence cs) {
		return new JsonNumberValue(cs);
	}
	
	public JsonNumberValue number(Number n) {
		return new JsonNumberValue(n);
	}
	
	public JsonNumberValue number(int n) {
		return number(Integer.valueOf(n));
	}
	
	public JsonNumberValue number(long n) {
		return number(Long.valueOf(n));
	}
	
	public JsonNumberValue number(float n) {
		return number(Float.valueOf(n));
	}
	
	public JsonNumberValue number(double n) {
		return number(Double.valueOf(n));
	}
	
	
	/**
	 * 
	 * @param v
	 * @return STRING
	 */
	public JsonStringValue string(CharSequence v) {
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
	public JsonStringValue string(JsonString v) {
		return new JsonStringValue(v);
	}
	
	/**
	 * 
	 * @return blank ARRAY
	 */
	public JsonArrayValue array() {
		return SingletonHolder.emptyArrayValue;
	}
	
	/**
	 * 
	 * @param v
	 * @return ARRAY
	 */
	public JsonArrayValue array(JsonValue... v) {
		return new JsonArrayValue(Arrays.asList(v));
	}
	
	/**
	 * 
	 * @param v
	 * @return ARRAY
	 */
	public JsonArrayValue array(List<? extends JsonValue> v) {
		if ( v.isEmpty() ) {
			return array();
		} else {
			return new JsonArrayValue(v);
		}
	}
	
	
	/**
	 * 
	 * @return blank OBJECT
	 */
	public JsonObjectValue object() {
		return SingletonHolder.emptyObjectValue;
	}
	
	/**
	 * 
	 * @param v
	 * @return OBJECT
	 */
	public JsonObjectValue object(JsonObjectPair... v) {
		return object(Arrays.asList(v));
	}

	/**
	 * 
	 * @param v
	 * @return OBJECT
	 */
	public JsonObjectValue object(Collection<? extends JsonObjectPair> v) {
		if ( v.isEmpty() ) {
			return object();
		} else {
			return new JsonObjectValue(v);
		}
	}
	
	/**
	 * 
	 * @param v
	 * @return OBJECT
	 */
	public JsonObjectValue object(Map<? extends JsonString, ? extends JsonValue> v) {
		
		final Collection<JsonObjectPair> pairs = new ArrayList<>();
		
		Objects.requireNonNull(v).forEach((name, value) -> {
			pairs.add(pair(name, value));
		});
			
		return object(pairs);
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
		return JsonValueJsonParser.getInstance().parse(v);
	}
	
	/**
	 * 
	 * @param reader
	 * @return JsonValue
	 * @throws IOException
	 * @throws JsonValueParseException
	 */
	public JsonValue fromJson(Reader reader) throws IOException {
		return JsonValueJsonParser.getInstance().parse(reader);
	}
	
}
