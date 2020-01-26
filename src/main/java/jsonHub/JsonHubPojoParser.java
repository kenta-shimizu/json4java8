package jsonHub;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonHubPojoParser {

	private JsonHubPojoParser() {
		/* Nothign */
	}
	
	private static class SingletonHolder {
		private static final JsonHubPojoParser inst = new JsonHubPojoParser();
	}
	
	public static JsonHubPojoParser getInstance() {
		return SingletonHolder.inst;
		
		
	}
	
	public JsonHub fromPojo(Object pojo) {
		try {
			return fromObjectPojo(pojo);
		}
		catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new JsonHubParseException(e);
		}
	}
	
	private static JsonHub fromObjectPojo(Object pojo)
			throws IllegalArgumentException, IllegalAccessException {
		
		final JsonHubBuilder jhb = JsonHubBuilder.getInstance();
		
		if ( pojo == null ) {
			
			return jhb.nullValue();
		
		} else if ( pojo.getClass().isArray() ) {
			
			final List<JsonHub> ll = new ArrayList<>();
			
			int len = Array.getLength(pojo);
			
			for ( int i = 0 ; i < len; ++i ) {
				ll.add(fromObjectPojo(Array.get(pojo, i)));
			}
			
			return jhb.array(ll);
			
		} else if ( pojo instanceof Boolean ) {
			
			return jhb.build(((Boolean)pojo).booleanValue());
			
		} else if ( pojo instanceof CharSequence ) {
			
			return jhb.string((CharSequence)pojo);
			
		} else if ( pojo instanceof Number ) {
			
			return jhb.number((Number)pojo);
			
		} else if ( pojo instanceof List<?> ) {
			
			List<?> pojoList = (List<?>)pojo;
			
			final List<JsonHub> ll = new ArrayList<>();
			
			for ( Object p : pojoList ) {
				ll.add(fromObjectPojo(p));
			}
			
			return jhb.array(ll);
		}
		
		
		final List<JsonObjectPair> pairs = new ArrayList<>();
		
		for ( Field field : pojo.getClass().getFields() ) {
			
			field.setAccessible(true);
			
			int iMod = field.getModifiers();
			
			if ( ! Modifier.isPublic(iMod) ) {
				continue;
			}

			if ( Modifier.isStatic(iMod) ) {
				continue;
			}
			
			pairs.add(
					jhb.pair(
							field.getName()
							, fromObjectPojo(field.get(pojo))));
		}
		
		return jhb.object(pairs);
	}
	
	public <T> T toPojo(JsonHub jh, Class<T> classOfT) {
		
		try {
			return toTopLevelPojo(jh, classOfT);
		}
		catch (ReflectiveOperationException | ClassCastException | IllegalArgumentException e) {
			throw new JsonHubParseException(e);
		}
	}
	
	private static <T> T toTopLevelPojo(JsonHub jh, Class<T> classOfT)
		throws ReflectiveOperationException {
		
		switch ( jh.type() ) {
		case NULL: {
			
			return null;
			/* break */
		}
		case TRUE:
		case FALSE: {
			
			Boolean f = jh.optionalBoolean().get();
			return classOfT.cast(f);
			/* break; */
		}
		case STRING: {
			
			return classOfT.cast(jh.toString());
			/* break */
		}
		case NUMBER: {
			
			Number n = toNumberPojo(
					jh.optionalNubmer().get()
					, classOfT);
			
			return classOfT.cast(n);
			/* break */
		}
		case ARRAY : {
			
			if ( classOfT.isArray() ) {
				
				return toArrayPojo(jh, classOfT);
				
			} else {
				
				//TODO
				
				/* Not beautiful */
				throw new JsonHubUnsupportedParseException("Top level \"" + classOfT.toString() + "\" not supported");
			}
			
			/* break; */
		}
		case OBJECT: {
			
			return toObjectPojo(jh, classOfT);
			/* break */
		}
		default: {
			return null;
		}
		}
	}
	
	private static <T> T toObjectPojo(JsonHub jh, Class<T> classOfT)
			throws ReflectiveOperationException {
		
		final T inst = classOfT.getDeclaredConstructor().newInstance();
		
		for ( JsonObjectPair pair : jh.objectPairs() ) {
			
			try {
				
				String name = pair.name().unescaped();
				
				Field field = classOfT.getField(name);
				
				field.setAccessible(true);
				
				{
					int iMod = field.getModifiers();
					
					if ( ! Modifier.isPublic(iMod) ) {
						continue;
					}
					
					if ( Modifier.isStatic(iMod) ) {
						continue;
					}
					
					if ( Modifier.isFinal(iMod) ) {
						continue;
					}
				}
				
				JsonHub v = pair.value();
				
				switch ( v.type() ) {
				case NULL: {
					
					field.set(inst, null);
					break;
				}
				case TRUE:
				case FALSE: {
					
					field.set(inst, v.optionalBoolean().get());
					break;
				}
				case STRING: {
					
					field.set(inst, v.toString());
					break;
				}
				case NUMBER: {
					
					Number n = v.optionalNubmer().get();
					field.set(inst, toNumberPojo(n, field.getType()));
					break;
				}
				case ARRAY: {
					
					Type type = field.getGenericType();
					
					if ( type instanceof Class<?> ) {
						
						Class<?> fieldClass = (Class<?>)type;
						
						if ( ! fieldClass.isArray() ) {
							throw new JsonHubUnsupportedParseException("\"" +type.toString() + "\" is not support");
						}
						
						try {
							
							field.set(inst, toArrayPojo(
									v
									, fieldClass));
						}
						catch ( ClassNotFoundException e ) {
							throw new JsonHubUnsupportedParseException("\"" +type.toString() + "\" is not support", e);
						}
						
					} else if ( type instanceof ParameterizedType ) {
						
						field.set(inst, toUtilListPojo(
								v
								, (ParameterizedType)type));
						
					} else {
						
						throw new JsonHubUnsupportedParseException("\"" +type.toString() + "\" is not support");
					}
					
					break;
				}
				case OBJECT: {
					
					field.set(inst, toObjectPojo(v, field.getType()));
					break;
				}
				}
			}
			catch ( NoSuchFieldException ignore ) {
			}
		}
		
		return inst;
	}
	
	private static <T> T toArrayPojo(JsonHub jh, Class<T> classOfT)
			throws ReflectiveOperationException {
		
		Class<?> compClass = classOfT.getComponentType();
		
		int len = jh.length();
		
		Object array = Array.newInstance(compClass, len);
		
		for ( int i = 0; i < len; ++i ) {
			
			JsonHub v = jh.get(i);
			
			switch ( v.type() ) {
			case NULL: {
				
				Array.set(array, i, null);
				break;
			}
			case TRUE:
			case FALSE: {
				
				Array.set(array, i, v.optionalBoolean().get());
				break;
			}
			case STRING: {
				
				Array.set(array, i, v.toString());
				break;
			}
			case NUMBER: {
				
				Number n = v.optionalNubmer().get();
				Array.set(array, i, toNumberPojo(n, compClass));
				break;
			}
			case ARRAY: {
				
				if ( compClass.isArray() ) {
					
					Array.set(array, i, toArrayPojo(v, compClass));
					
				} else {
					
					throw new JsonHubUnsupportedParseException("Cannot create a generic Array");
				}
				break;
			}
			case OBJECT: {
				
				Array.set(array, i, toObjectPojo(v, compClass));
				break;
			}

			}
		}
		
		return classOfT.cast(array);
	}
	
	private static Object toUtilListPojo(JsonHub jh, ParameterizedType type)
			throws ReflectiveOperationException {
		
		if ( ! jh.isArray() ) {
			throw new JsonHubUnsupportedParseException("\"" +type.toString() + "\" is not support");
		}
		
		Type ptype = type.getActualTypeArguments()[0];
		
		if ( ptype instanceof Class<?> ) {
			
			try {
				return toUtilListPojo(jh, (Class<?>)ptype);
			}
			catch ( ClassNotFoundException e ) {
				throw new JsonHubUnsupportedParseException("\"" +ptype.toString() + "\" is not support", e);
			}
			
		} else if ( ptype instanceof ParameterizedType ) {
			
			List<Object> ll = new ArrayList<>();
			
			for ( JsonHub v : jh.values() ) {
				ll.add(toUtilListPojo(v, (ParameterizedType)ptype));
			}
			
			return ll;
			
		} else {
			
			throw new JsonHubUnsupportedParseException("\"" + ptype.toString() + "\" is not support");
		}
	}
	
	private static <T> List<T> toUtilListPojo(JsonHub jh, Class<T> classOfT)
			throws ReflectiveOperationException {
		
		List<T> inst = new ArrayList<>();
		
		for ( JsonHub v : jh ) {
			
			switch ( v.type() ) {
			case NULL: {
				
				inst.add(null);
				break;
			}
			case TRUE:
			case FALSE: {
				
				inst.add(classOfT.cast(v.optionalBoolean().get()));
				break;
			}
			case STRING: {
				
				inst.add(classOfT.cast(v.toString()));
				break;
			}
			case NUMBER: {
				
				Number n = v.optionalNubmer().get();
				inst.add(classOfT.cast(toNumberPojo(n, classOfT)));
				break;
			}
			case ARRAY: {
				
				if ( classOfT.isArray() ) {
					
					inst.add(toArrayPojo(v, classOfT));
					
				} else {
					
					throw new JsonHubUnsupportedParseException("Cannot create a generic Array");
				}
				break;
			}
			case OBJECT: {
				
				inst.add(toObjectPojo(v, classOfT));
				break;
			}
			}
		}
		
		return inst;
	}
	
	private static <T> Number toNumberPojo(Number n, Class<?> classOfT) {
		
		if ( classOfT == byte.class || classOfT == Byte.class ) {
			return Byte.valueOf(n.byteValue());
		}
		
		if ( classOfT == short.class || classOfT == Short.class ) {
			return Short.valueOf(n.shortValue());
		}
		
		if ( classOfT == int.class || classOfT == Integer.class ) {
			return Integer.valueOf(n.intValue());
		}
		
		if ( classOfT == long.class || classOfT == Long.class ) {
			return Long.valueOf(n.longValue());
		}
		
		if ( classOfT == float.class || classOfT == Float.class ) {
			return Float.valueOf(n.floatValue());
		}
		
		if ( classOfT == double.class || classOfT == Double.class ) {
			return Double.valueOf(n.doubleValue());
		}
		
		throw new JsonHubParseException("toNumberPojo cast failed \"" + classOfT.toString() + "\"");
	}
	
}
