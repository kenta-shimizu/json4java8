package com.shimizukenta.jsonhub.impl;

import java.io.Serializable;
import java.util.Objects;

import com.shimizukenta.jsonhub.JsonHub;
import com.shimizukenta.jsonhub.JsonObjectPair;
import com.shimizukenta.jsonhub.JsonString;

/**
 * This class is implements of name and value pair in JSON-OBJECT.
 * 
 * @author kenta-shimizu
 *
 */
public class JsonObjectPairImpl implements Serializable, JsonObjectPair {
	
	private static final long serialVersionUID = 5500254554321018084L;
	
	/**
	 * Name of Object
	 */
	private final JsonString name;
	
	/**
	 * Value of Object
	 */
	private final JsonHub v;
	
	/**
	 * Constructor.
	 * 
	 * @param name the name
	 * @param v the value
	 */
	public JsonObjectPairImpl(JsonString name, JsonHub v) {
		this.name = Objects.requireNonNull(name, "JsonObjectPair nonNull \"name\"");
		this.v = Objects.requireNonNull(v, "JsonObjectPair nonNull \"value\"");
	}
	
	@Override
	public JsonString name() {
		return name;
	}
	
	@Override
	public JsonHub value() {
		return v;
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof JsonObjectPairImpl)) {
			return ((JsonObjectPairImpl) o).name().equals(name());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name().hashCode();
	}
	
	@Override
	public String toString() {
		return "(\"" + name() + "\": " + value() + ")";
	}
}
