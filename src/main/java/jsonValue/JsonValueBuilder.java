package jsonValue;

public class JsonValueBuilder {

	private JsonValueBuilder() {
		/* Nothing */
	}
	
	private static class SingletonHolder {
		private static final JsonValueBuilder inst = new JsonValueBuilder();
	}
	
	public static JsonValueBuilder getInstance() {
		return SingletonHolder.inst;
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
			return JsonValue.trueValue();
		} else {
			return JsonValue.falseValue();
		}
	}
	
	public JsonValue build(Object v) {
		
		if ( v == null ) {
			
			return JsonValue.nullValue();
			
		} else {
			
			if ( v instanceof CharSequence ) {
				return new JsonStringValue((CharSequence)v);
			}
			
			if ( v instanceof Number ) {
				return new JsonNumberValue(((Number)v).toString());
			}
			
			if ( v instanceof Boolean ) {
				build((Boolean) v).booleanValue();
			}
			
			
			//TODO
			throw new RuntimeException();
		}
	}
	
}
