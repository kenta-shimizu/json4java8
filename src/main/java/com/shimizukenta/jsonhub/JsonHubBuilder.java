package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This interface is implements of building JsonHub instance.
 * 
 * <p>
 * This class is used in {@link JsonReader}, {@link JsonHubFromPojoParser}.<br />
 * </p>
 * <p>
 * To build NullJsonHub instance, {@link #nullValue()}, {@link #build(Object)}.<br />
 * To build TrueJsonHub instance, {@link #trueValue()}, {@link #build(Object)}.<br />
 * To build FalseJsonHub instance, {@link #falseValue()}, {@link #build(Object)}.<br />
 * To build StringJsonHub instance, {@link #string(CharSequence)}, {@link #string(JsonString)}, {@link #build(Object)}.<br />
 * To build NumberJsonHub instance, {@link #number(int)}, {@link #number(long)}, {@link #number(float)}, {@link #number(double)},
 * {@link #number(Number)}, {@link #number(CharSequence)},
 * {@link #build(int)}, {@link #build(long)}, {@link #build(float)}, {@link #build(double)}, {@link #build(Object)}.<br />
 * To build ArrayJsonHub instance, {@link #array()}, {@link #array(JsonHub...)}, {@link #array(List)}, {@link #emptyArray()}.<br />
 * To build ObjectJsonHub instance, {@link #object()}, {@link #object(Collection)}, {@link #object(JsonObjectPair...)}, {@link #object(Map)}, {@link #emptyObject()}.<br />
 * </p>
 * <p>
 * To build JsonObjectPair instance, {@link #pair(CharSequence, boolean)},
 * {@link #pair(CharSequence, int)}, {@link #pair(CharSequence, long)},
 * {@link #pair(CharSequence, float)}, {@link #pair(CharSequence, double)},
 * {@link #pair(CharSequence, Object)},
 * {@link #pair(JsonString, boolean)},
 * {@link #pair(JsonString, int)}, {@link #pair(JsonString, long)},
 * {@link #pair(JsonString, float)}, {@link #pair(JsonString, double)},
 * {@link #pair(JsonString, Object)}.<br />
 * </p>
 * 
 * @author kenta-shimizu
 *
 */
public interface JsonHubBuilder {
	
	/**
	 * Returns JsonHubBuilder instance.
	 * 
	 * <p>
	 * This builder is Singleton pattern.
	 * </p>
	 * 
	 * @return JsonHubBuilder instance.
	 */
	public static JsonHubBuilder getInstance() {
		return AbstractJsonHubBuilder.getInstance();
	}
	
	/**
	 * Returns NullJsonHub instance.
	 * 
	 * <p>
	 * This instance is Singleton-pattern.<br />
	 * Returned instances are all the same.<br />
	 * </p>
	 * 
	 * @return NullJsonHub instance
	 */
	public NullJsonHub nullValue();

	/**
	 * Returns TrueJsonHub instance.
	 * 
	 * <p>
	 * This instance is Singleton-pattern.<br />
	 * Returned instances are all the same.<br />
	 * </p>
	 * 
	 * @return TrueJsonHub instance
	 */
	public TrueJsonHub trueValue();

	/**
	 * Returns FalseJsonHub instance.
	 * 
	 * <p>
	 * This instance is Singleton-pattern.<br />
	 * Returned instances are all the same.<br />
	 * </p>
	 * 
	 * @return FalseJsonHub instance
	 */
	public FalseJsonHub falseValue();

	/**
	 * Returns NumberJsonHub instance of int-value.
	 * 
	 * @param v
	 * @return NumberJsonHub instance of int-value
	 */
	public NumberJsonHub build(int v);

	/**
	 * Returns NumberJsonHub instance of long-value.
	 * 
	 * @param v
	 * @return NumberJsonHub instance of long-value
	 */
	public NumberJsonHub build(long v);

	/**
	 * Returns NumberJsonHub instance of float-value.
	 * 
	 * @param v
	 * @return NumberJsonHub instance of float-value
	 */
	public NumberJsonHub build(float v);

	/**
	 * Returns NumberJsonHub instance of double-value.
	 * 
	 * @param v
	 * @return NumberJsonHub instance of double-value
	 */
	public NumberJsonHub build(double v);

	/**
	 * Returns TrueJsonHub or FalseJsonHub instance.
	 * 
	 * @param v
	 * @return TrueJsonHub instance if true, and FalseJsonHub instance otherwise
	 */
	public AbstractJsonHub build(boolean v);

	/**
	 * Returns AbstractJsonHub instance (NULL, TRUE, FALSE, STRING or NUMBER).
	 * 
	 * <p>
	 * If {@code v == null}, return NullJsonHub instance.<br />
	 * If {@code v instanceof Boolean}, return TrueJsonHub or FalseJsonHub instance.<br />
	 * If {@code v instanceof CharSequence}, return StringJsonHub instance.<br />
	 * If {@code v instanceof JsonString}, return StringJsonHub instance.<br />
	 * If {@code v instanceof Number}, return NumberJsonHub instance.<br />
	 * </p>
	 * 
	 * @param v
	 * @return AbstractJsonHub (NULL, TRUE, FALSE, STRING or NUMBER)
	 * @throws JsonHubBuildException if type is unsupported
	 */
	public AbstractJsonHub build(Object v);

	/**
	 * Returns NumberJsonHub instance from JSON-String.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param cs Number-String
	 * @return NumberJsonHub
	 * @throws JsonHubNumberFormatException
	 */
	public NumberJsonHub number(CharSequence cs);

	/**
	 * Returns NumberJsonHub instance.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param n
	 * @return NumberJsonHub instance
	 */
	public NumberJsonHub number(Number n);

	/**
	 * Returns NumberJsonHub instance of int-value.
	 * 
	 * @param n
	 * @return NumberJsonHub instance of int-value
	 */
	public NumberJsonHub number(int n);

	/**
	 * Returns NumberJsonHub instance of long-value.
	 * 
	 * @param n
	 * @return NumberJsonHub instance of long-value
	 */
	public NumberJsonHub number(long n);

	/**
	 * Returns NumberJsonHub instance of float-value.
	 * 
	 * @param n
	 * @return NumberJsonHub instance of float-value
	 */
	public NumberJsonHub number(float n);

	/**
	 * Returns NumberJsonHub instance of double-value.
	 * 
	 * @param n
	 * @return NumberJsonHub instance of double-value
	 */
	public NumberJsonHub number(double n);

	/**
	 * Returns StringJsonHub from JSON-String.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param v
	 * @return StringJsonHub
	 */
	public StringJsonHub string(CharSequence v);

	/**
	 * Returns StringJsonHub from JsonString.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param v
	 * @return StringJsonHub
	 */
	public StringJsonHub string(JsonString v);

	/**
	 * Returns ArrayJsonHub instance, values is empty.
	 * 
	 * <p>
	 * This instance is Singleton-pattern.<br />
	 * Returned instances are all the same.<br />
	 * </p>
	 * 
	 * @return empty-ArrayJsonHub instance
	 */
	public ArrayJsonHub array();

	/**
	 * Returns ArrayJsonHub instance.
	 * 
	 * @param values
	 * @return ArrayJsonHub instance
	 */
	public ArrayJsonHub array(JsonHub... values);

	/**
	 * Returns ArrayJsonHub instance.
	 * 
	 * @param values
	 * @return ArrayJsonHub instance
	 */
	public ArrayJsonHub array(List<? extends JsonHub> values);

	/**
	 * Returns ArrayJsonHub instance, values is empty.
	 * 
	 * <p>
	 * This instance is Singleton-pattern.<br />
	 * Returned instances are all the same.<br />
	 * </p>
	 * 
	 * @return empty-ArrayJsonHub instance
	 */
	public ArrayJsonHub emptyArray();

	/**
	 * Returns ObjectJsonHub instance, Object pairs is empty.
	 * 
	 * <p>
	 * This instance is Singleton-pattern.<br />
	 * Returned instances are all the same.<br />
	 * </p>
	 * 
	 * @return empty-ObjectJsonHub instance
	 */
	public ObjectJsonHub object();

	/**
	 * Returns ObjectJsonHub instance.
	 * 
	 * @param pairs
	 * @return ObjectJsonHub instance
	 */
	public ObjectJsonHub object(JsonObjectPair... pairs);

	/**
	 * Returns ObjectJsonHub instance.
	 * 
	 * @param pairs
	 * @return ObjectJsonHub instance
	 */
	public ObjectJsonHub object(Collection<? extends JsonObjectPair> pairs);

	/**
	 * Returns ObjectJsonHub instance.
	 * 
	 * @param map
	 * @return ObjectJsonHub instance
	 */
	public ObjectJsonHub object(Map<? extends JsonString, ? extends JsonHub> map);

	/**
	 * Returns ObjectJsonHub instance, Object pairs is empty.
	 * 
	 * <p>
	 * This instance is Singleton-pattern.<br />
	 * Returned instances are all the same.<br />
	 * </p>
	 * 
	 * @return empty-ObjectJsonHub instance
	 */
	public ObjectJsonHub emptyObject();

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 * @throws JsonHubBuildException if unsupported Object type
	 */
	public JsonObjectPair pair(JsonString name, Object value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, int value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, long value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, float value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, double value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, boolean value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 * @throws JsonHubBuildException if unsupported Object type
	 */
	public JsonObjectPair pair(CharSequence name, Object value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, int value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, long value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, float value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, double value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name
	 * @param value
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, boolean value);

	/**
	 * Returns parsed JsonHub instance from JSON-String.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param v 
	 * @return parsed JsonHub instance
	 * @throws JsonHubParseException if parse failed
	 */
	public JsonHub fromJson(CharSequence v);

	/**
	 * Returns parsed JsonHub instance from Reader includes JSON-String.
	 * 
	 * @param reader includes JSON-String
	 * @return parsed JsonHub instance
	 * @throws IOException
	 * @throws JsonHubParseException if parse failed
	 */
	public JsonHub fromJson(Reader reader) throws IOException;

}
