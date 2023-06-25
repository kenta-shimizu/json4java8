package com.shimizukenta.jsonhub;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.shimizukenta.jsonhub.impl.AbstractJsonHubBuilder;

/**
 * This interface is implements of building JsonHub instance.
 * 
 * <ul>
 * <li>To build null, build(null) or {@link #nullValue()}.</li>
 * <li>To build true, build(true) or {@link #trueValue()}.</li>
 * <li>To build false, build(false) or {@link #falseValue()}.</li>
 * <li>To build string, build(string) or {@link #string(CharSequence)}.</li>
 * <li>To build number,
 * <ul>
 * <li>{@link #build(int)} or {@link #number(int)}</li>
 * <li>{@link #build(long)} or {@link #number(long)}.</li>
 * <li>{@link #build(float)} or {@link #number(float)}.</li>
 * <li>{@link #build(double)} or {@link #number(double)}.</li>
 * <li>{@link #number(Number)}.</li>
 * </ul>
 * </li>
 * <li>To build array,
 * <ul>
 * <li>{@link #array(JsonHub...)}.</li>
 * <li>{@link #array(List)}.</li>
 * </ul>
 * </li>
 * <li>To build object,
 * <ul>
 * <li>{@link #object(JsonObjectPair...)}.</li>
 * <li>{@link #object(Collection)}.</li>
 * <li>{@link #object(Map)}.</li>
 * </ul>
 * </li>
 * </ul>
 * <ul>
 * <li>To build JsonObjectPair(name and value),
 * <ul>
 * <li>{@link #pair(CharSequence, boolean)}.</li>
 * <li>{@link #pair(CharSequence, int)}.</li>
 * <li>{@link #pair(CharSequence, long)}.</li>
 * <li>{@link #pair(CharSequence, float)}.</li>
 * <li>{@link #pair(CharSequence, double)}.</li>
 * <li>{@link #pair(CharSequence, Object)}.</li>
 * </ul>
 * </li>
 * </ul>
 * <pre>
 * // Example of Build.
 * 
 * JsonHubBuilder jhb = JsonHubBuilder.getInstance().
 * 
 * JsonHub jh = jhb.object(
 *     jhb.pair("str", "STRING"),
 *     jhb.pair("num", 100),
 *     jhb.pair("bool", true),
 *     jhb.pair("array", jhb.array(
 *         jhb.build("a"),
 *         jhb.build("b"),
 *         jhb.build("c")
 *     ))
 * );
 * 
 * String json = jh.toJson();
 * 
 * System.out.println(json); //{"str":"STRING","num":100,"bool":true,"array":["a","b","c"]}
 * </pre>
 * 
 * @author kenta-shimizu
 * @see JsonObjectPair
 * @see JsonString
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
	public JsonHub nullValue();

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
	public JsonHub trueValue();

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
	public JsonHub falseValue();

	/**
	 * Returns NumberJsonHub instance of int-value.
	 * 
	 * @param value int number
	 * @return NumberJsonHub instance of int-value
	 */
	public JsonHub build(int value);

	/**
	 * Returns NumberJsonHub instance of long-value.
	 * 
	 * @param value long number
	 * @return NumberJsonHub instance of long-value
	 */
	public JsonHub build(long value);

	/**
	 * Returns NumberJsonHub instance of float-value.
	 * 
	 * @param value float number
	 * @return NumberJsonHub instance of float-value
	 */
	public JsonHub build(float value);

	/**
	 * Returns NumberJsonHub instance of double-value.
	 * 
	 * @param value double number
	 * @return NumberJsonHub instance of double-value
	 */
	public JsonHub build(double value);

	/**
	 * Returns TrueJsonHub or FalseJsonHub instance.
	 * 
	 * @param value boolean value
	 * @return TrueJsonHub instance if true, and FalseJsonHub instance otherwise
	 */
	public JsonHub build(boolean value);

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
	 * @param value Object value
	 * @return JsonHub (NULL, TRUE, FALSE, STRING or NUMBER)
	 * @throws JsonHubBuildException if type is unsupported
	 */
	public JsonHub build(Object value);

	/**
	 * Returns NumberJsonHub instance from JSON-String.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param value Number-String
	 * @return NumberJsonHub
	 * @throws JsonHubNumberFormatException if Number format failed
	 */
	public JsonHub number(CharSequence value);

	/**
	 * Returns NumberJsonHub instance.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param value Number value
	 * @return NumberJsonHub instance
	 */
	public JsonHub number(Number value);

	/**
	 * Returns NumberJsonHub instance of int-value.
	 * 
	 * @param value int number
	 * @return NumberJsonHub instance of int-value
	 */
	public JsonHub number(int value);

	/**
	 * Returns NumberJsonHub instance of long-value.
	 * 
	 * @param value long number
	 * @return NumberJsonHub instance of long-value
	 */
	public JsonHub number(long value);

	/**
	 * Returns NumberJsonHub instance of float-value.
	 * 
	 * @param value float number
	 * @return NumberJsonHub instance of float-value
	 */
	public JsonHub number(float value);

	/**
	 * Returns NumberJsonHub instance of double-value.
	 * 
	 * @param value double number
	 * @return NumberJsonHub instance of double-value
	 */
	public JsonHub number(double value);

	/**
	 * Returns StringJsonHub from JSON-String.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param value charsequence
	 * @return StringJsonHub
	 */
	public JsonHub string(CharSequence value);

	/**
	 * Returns StringJsonHub from JsonString.
	 * 
	 * <p>
	 * Not accept {@code null}.<br />
	 * </p>
	 * 
	 * @param value JsonString
	 * @return StringJsonHub
	 */
	public JsonHub string(JsonString value);

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
	public JsonHub array();

	/**
	 * Returns ArrayJsonHub instance.
	 * 
	 * @param values Array of JsonHub
	 * @return ArrayJsonHub instance
	 */
	public JsonHub array(JsonHub... values);

	/**
	 * Returns ArrayJsonHub instance.
	 * 
	 * @param values List of JsonHub
	 * @return ArrayJsonHub instance
	 */
	public JsonHub array(List<? extends JsonHub> values);

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
	public JsonHub emptyArray();

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
	public JsonHub object();

	/**
	 * Returns ObjectJsonHub instance.
	 * 
	 * @param pairs pairs of name and value
	 * @return ObjectJsonHub instance
	 * @see JsonObjectPair
	 */
	public JsonHub object(JsonObjectPair... pairs);

	/**
	 * Returns ObjectJsonHub instance.
	 * 
	 * @param pairs pairs of name and value
	 * @return ObjectJsonHub instance
	 * @see JsonObjectPair
	 */
	public JsonHub object(Collection<? extends JsonObjectPair> pairs);

	/**
	 * Returns ObjectJsonHub instance.
	 * 
	 * @param map Map
	 * @return ObjectJsonHub instance
	 */
	public JsonHub object(Map<? extends JsonString, ? extends JsonHub> map);

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
	public JsonHub emptyObject();

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value value of Object
	 * @return JsonObjectPair
	 * @throws JsonHubBuildException if unsupported Object type
	 */
	public JsonObjectPair pair(JsonString name, Object value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value int value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, int value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value long value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, long value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value float value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, float value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value double value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, double value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value boolean value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(JsonString name, boolean value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value Object value of Object
	 * @return JsonObjectPair
	 * @throws JsonHubBuildException if unsupported Object type
	 */
	public JsonObjectPair pair(CharSequence name, Object value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value int value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, int value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value long value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, long value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value float value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, float value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value double value of Object
	 * @return JsonObjectPair
	 */
	public JsonObjectPair pair(CharSequence name, double value);

	/**
	 * Returns JsonObjectPair instance.
	 * 
	 * @param name name of Object
	 * @param value boolean value of Object
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
	 * @param json JSON-string
	 * @return parsed JsonHub instance
	 * @throws JsonHubParseException if parse failed
	 */
	public JsonHub fromJson(CharSequence json);

	/**
	 * Returns parsed JsonHub instance from Reader includes JSON-String.
	 * 
	 * @param reader includes JSON-String
	 * @return parsed JsonHub instance
	 * @throws IOException if IO-Except
	 * @throws JsonHubParseException if parse failed
	 */
	public JsonHub fromJson(Reader reader) throws IOException;

}
