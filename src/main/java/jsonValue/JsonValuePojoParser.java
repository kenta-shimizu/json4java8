package jsonValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JsonValuePojoParser {

	private JsonValuePojoParser() {
		/* Nothign */
	}
	
	private static class SingletonHolder {
		private static final JsonValuePojoParser inst = new JsonValuePojoParser();
	}
	
	public static JsonValuePojoParser getInstance() {
		return SingletonHolder.inst;
	}
	
	public JsonValue fromPojo(Object pojo) {
		try {
			if ( pojo == null ) {
				return JsonValueBuilder.getInstance().nullValue();
			} else {
				synchronized ( pojo ) {
					return fromObjectPojo(pojo);
				}
			}
		}
		catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new JsonValueParseException(e);
		}
	}
	
	private static JsonValue fromObjectPojo(Object pojo)
			throws IllegalArgumentException, IllegalAccessException {
		
		final JsonValueBuilder jvb = JsonValueBuilder.getInstance();
		
		if ( pojo == null ) {
			
			return jvb.nullValue();
			
		} else if ( pojo instanceof Boolean ) {
			
			return jvb.build(((Boolean)pojo).booleanValue());
			
		} else if ( pojo instanceof CharSequence ) {
			
			return jvb.string((CharSequence)pojo);
			
		} else if ( pojo instanceof Number ) {
			
			return jvb.number((Number)pojo);
			
		} else if ( pojo instanceof List<?> ) {
			
			return fromListPojo((List<?>)pojo);
			
		}
		
		
		final List<JsonObjectPair> pairs = new ArrayList<>();
		
		for ( Field field : pojo.getClass().getFields() ) {
			
			field.setAccessible(true);
			
			int iMod = field.getModifiers();
			
			if (
					! Modifier.isStatic(iMod)
					&& ! Modifier.isFinal(iMod)
					) {
				
				pairs.add(
						jvb.pair(
								field.getName()
								, fromObjectPojo(field.get(pojo))));
			}
		}
		
		return jvb.object(pairs);
	}
	
	private static JsonArrayValue fromListPojo(List<?> pojoList)
			throws IllegalArgumentException, IllegalAccessException {
		
		final JsonValueBuilder jvb = JsonValueBuilder.getInstance();
		
		final List<JsonValue> ll = new ArrayList<>();
		
		for ( Object pojo : pojoList ) {
			ll.add(fromObjectPojo(pojo));
		}
		
		return jvb.array(ll);
	}
	
	public <T> T toPojo(JsonValue jv, Class<T> classOfT) {
		
		try {
			return toObjectPojo(jv, classOfT);
		}
		catch (
				InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e
				) {
			
			throw new JsonValueParseException(e);
		}
	}
	
	public <T> T toObjectPojo(JsonValue jv, Class<T> classOfT)
			throws IllegalArgumentException, IllegalAccessException
			,InstantiationException, InvocationTargetException
			, NoSuchMethodException, SecurityException {
		
		
		if ( jv.isNull() ) {
			return null;
		}
		
		//Long
		//Integer
		//Short
		//Byte
		//Double
		//Float
		//Boolean
		
		
		if ( classOfT == Integer.class) {
			
//			return jv.optionalNubmer()
//					.map(n -> n.intValue())
//					.orElse(null);
		}
		
		final T inst = classOfT.getDeclaredConstructor().newInstance();
		
		for ( JsonObjectPair pair : jv.objectPairs() ) {
			
			try {
				
				String name = pair.name().unescaped();
				
				Field field = classOfT.getField(name);
				
				field.setAccessible(true);
				
				{
					int iMod = field.getModifiers();
					
					if ( Modifier.isStatic(iMod) ) {
						continue;
					}
					
					if ( Modifier.isFinal(iMod) ) {
						continue;
					}
				}
				
				JsonValue v = pair.value();
				
				switch ( v.type() ) {
				case NULL: {
					
					field.set(inst, null);
					break;
				}
				case TRUE:
				case FALSE: {
					
					field.setBoolean(inst, v.booleanValue());
					break;
				}
				case STRING: {
					
					field.set(inst, v.toString());
					break;
				}
				case NUMBER: {
					
					Class<?> type = field.getType();
					Number n = v.optionalNubmer().orElseThrow(() -> new JsonValueNumberFormatException("\"" + name + "\" is not NUMBER"));
					
					if ( type == byte.class || type == Byte.class ) {
						
						field.setByte(inst, n.byteValue());
						
					} else if ( type == short.class || type == Short.class ) {
						
						field.setShort(inst, n.shortValue());
						
					} else if ( type == int.class || type == Integer.class ) {
						
						field.setInt(inst, n.intValue());
						
					} else if ( type == long.class || type == Long.class ) {
						
						field.setLong(inst, n.longValue());
						
					} else if ( type == float.class || type == Float.class ) {
						
						field.setFloat(inst, n.floatValue());
						
					} else if ( type == double.class || type == Double.class ) {
						
						field.setDouble(inst, n.doubleValue());
					}
					
					break;
				}
				case ARRAY: {
					
					//TODO
					
					break;
				}
				case OBJECT: {
					
					Class<?> deepClass = field.getType();
					field.set(inst, toObjectPojo(v, deepClass));
					break;
				}
				}
			}
			catch ( NoSuchFieldException ignore ) {
			}
		}
		
		return inst;
	}
	
	
	
}
