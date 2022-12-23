package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This abstract class is is implementation of building JsonHub.
 * 
 * @author kenta-shimizu
 *
 */
public abstract class AbstractJsonHubBuilder implements JsonHubBuilder {
	
	protected AbstractJsonHubBuilder() {
		/* Nothing */
	}
	
	private static class SingletonHolder {
		private static final AbstractJsonHubBuilder inst = new AbstractJsonHubBuilder() {};
		
		private static final JsonString emptyString = JsonString.escaped("");
		private static final NullJsonHub nullValue = new NullJsonHub();
		private static final TrueJsonHub trueValue = new TrueJsonHub();
		private static final FalseJsonHub falseValue = new FalseJsonHub();
		private static final ArrayJsonHub emptyArrayValue = new ArrayJsonHub(Collections.emptyList());
		private static final ObjectJsonHub emptyObjectValue = new ObjectJsonHub(Collections.emptyList());
		private static final StringJsonHub emptyStringValue = new StringJsonHub(emptyString);
	}
	
	/**
	 * Returns Builder instance.
	 * 
	 * <p>
	 * This class is Singleton-pattern.
	 * </p>
	 * 
	 * @return builder instance
	 */
	public static AbstractJsonHubBuilder getInstance() {
		return SingletonHolder.inst;
	}
	
	@Override
	public NullJsonHub nullValue() {
		return SingletonHolder.nullValue;
	}
	
	@Override
	public TrueJsonHub trueValue() {
		return SingletonHolder.trueValue;
	}
	
	@Override
	public FalseJsonHub falseValue() {
		return SingletonHolder.falseValue;
	}
	
	@Override
	public NumberJsonHub build(int v) {
		return number(v);
	}
	
	@Override
	public NumberJsonHub build(long v) {
		return number(v);
	}
	
	@Override
	public NumberJsonHub build(float v) {
		return number(v);
	}
	
	@Override
	public NumberJsonHub build(double v) {
		return number(v);
	}
	
	@Override
	public AbstractJsonHub build(boolean v) {
		if ( v ) {
			return trueValue();
		} else {
			return falseValue();
		}
	}
	
	@Override
	public AbstractJsonHub build(Object v) {
		
		if ( v == null ) {
			
			return nullValue();
			
		} else {
			
			if ( v instanceof Boolean ) {
				return build(((Boolean) v).booleanValue());
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
	
	@Override
	public NumberJsonHub number(CharSequence cs) {
		return new NumberJsonHub(cs);
	}
	
	@Override
	public NumberJsonHub number(Number n) {
		return new NumberJsonHub(n);
	}
	
	@Override
	public NumberJsonHub number(int n) {
		return number(Integer.valueOf(n));
	}
	
	@Override
	public NumberJsonHub number(long n) {
		return number(Long.valueOf(n));
	}
	
	@Override
	public NumberJsonHub number(float n) {
		return number(Float.valueOf(n));
	}
	
	@Override
	public NumberJsonHub number(double n) {
		return number(Double.valueOf(n));
	}
	
	@Override
	public StringJsonHub string(CharSequence v) {
		if ( Objects.requireNonNull(v).toString().isEmpty() ) {
			return SingletonHolder.emptyStringValue;
		} else {
			return string(JsonString.unescaped(v));
		}
	}
	
	@Override
	public StringJsonHub string(JsonString v) {
		return new StringJsonHub(v);
	}
	
	@Override
	public ArrayJsonHub array() {
		return emptyArray();
	}
	
	@Override
	public ArrayJsonHub array(JsonHub... values) {
		return new ArrayJsonHub(Arrays.asList(values));
	}
	
	@Override
	public ArrayJsonHub array(List<? extends JsonHub> values) {
		if ( Objects.requireNonNull(values).isEmpty() ) {
			return emptyArray();
		} else {
			return new ArrayJsonHub(values);
		}
	}
	
	@Override
	public ArrayJsonHub emptyArray() {
		return SingletonHolder.emptyArrayValue;
	}
	
	@Override
	public ObjectJsonHub object() {
		return emptyObject();
	}
	
	@Override
	public ObjectJsonHub object(JsonObjectPair... pairs) {
		return object(Arrays.asList(pairs));
	}

	@Override
	public ObjectJsonHub object(Collection<? extends JsonObjectPair> pairs) {
		if ( Objects.requireNonNull(pairs).isEmpty() ) {
			return emptyObject();
		} else {
			return new ObjectJsonHub(pairs);
		}
	}
	
	@Override
	public ObjectJsonHub object(Map<? extends JsonString, ? extends JsonHub> map) {
		
		final Collection<JsonObjectPair> pairs = new ArrayList<>();
		
		Objects.requireNonNull(map).forEach((name, value) -> {
			pairs.add(pair(name, value));
		});
			
		return object(pairs);
	}
	
	@Override
	public ObjectJsonHub emptyObject() {
		return SingletonHolder.emptyObjectValue;
	}
	
	@Override
	public JsonObjectPair pair(JsonString name, Object value) {
		if ((value != null) && (value instanceof JsonHub)) {
			return new JsonObjectPair(name, (JsonHub)value);
		} else {
			return new JsonObjectPair(name, build(value));
		}
	}
	
	@Override
	public JsonObjectPair pair(JsonString name, int value) {
		return new JsonObjectPair(name, number(value));
	}
	
	@Override
	public JsonObjectPair pair(JsonString name, long value) {
		return new JsonObjectPair(name, number(value));
	}
	
	@Override
	public JsonObjectPair pair(JsonString name, float value) {
		return new JsonObjectPair(name, number(value));
	}
	
	@Override
	public JsonObjectPair pair(JsonString name, double value) {
		return new JsonObjectPair(name, number(value));
	}
	
	@Override
	public JsonObjectPair pair(JsonString name, boolean value) {
		return new JsonObjectPair(name, build(value));
	}
	
	@Override
	public JsonObjectPair pair(CharSequence name, Object value) {
		return pair(JsonString.unescaped(name), value);
	}
	
	@Override
	public JsonObjectPair pair(CharSequence name, int value) {
		return pair(JsonString.unescaped(name), value);
	}
	
	@Override
	public JsonObjectPair pair(CharSequence name, long value) {
		return pair(JsonString.unescaped(name), value);
	}
	
	@Override
	public JsonObjectPair pair(CharSequence name, float value) {
		return pair(JsonString.unescaped(name), value);
	}
	
	@Override
	public JsonObjectPair pair(CharSequence name, double value) {
		return pair(JsonString.unescaped(name), value);
	}
	
	@Override
	public JsonObjectPair pair(CharSequence name, boolean value) {
		return pair(JsonString.unescaped(name), value);
	}
	
	@Override
	public JsonHub fromJson(CharSequence v) {
		return JsonReader.fromJson(v);
	}
	
	@Override
	public JsonHub fromJson(Reader reader) throws IOException {
		return JsonReader.fromJson(reader);
	}
	
}
