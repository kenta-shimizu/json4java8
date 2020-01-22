package jsonHub;

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
		
		final JsonHubBuilder jvb = JsonHubBuilder.getInstance();
		
		if ( pojo == null ) {
			
			return jvb.nullValue();
			
		} else if ( pojo instanceof Boolean ) {
			
			return jvb.build(((Boolean)pojo).booleanValue());
			
		} else if ( pojo instanceof CharSequence ) {
			
			return jvb.string((CharSequence)pojo);
			
		} else if ( pojo instanceof Number ) {
			
			return jvb.number((Number)pojo);
			
		} else if ( pojo instanceof List<?> ) {
			
			List<?> pojoList = (List<?>)pojo;
			
			final List<JsonHub> ll = new ArrayList<>();
			
			for ( Object p : pojoList ) {
				ll.add(fromObjectPojo(p));
			}
			
			return jvb.array(ll);
		}
		
		
		final List<JsonObjectPair> pairs = new ArrayList<>();
		
		for ( Field field : pojo.getClass().getFields() ) {
			
			field.setAccessible(true);
			
			int iMod = field.getModifiers();
			
			if ( Modifier.isStatic(iMod) ) {
				continue;
			}
			
			pairs.add(
					jvb.pair(
							field.getName()
							, fromObjectPojo(field.get(pojo))));
		}
		
		return jvb.object(pairs);
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
			
			return classOfT.cast(jh.optionalBoolean().get());
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
			
			throw new JsonHubParseException("Top level [] is not supported");
			/* break; */
		}
		case OBJECT: {
			
			return classOfT.cast(toObjectPojo(jh, classOfT));
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
					
					Type ptype = ((ParameterizedType)type)
							.getActualTypeArguments()[0];
					
					/* Not beautiful */
					field.set(inst, toArrayPojo(
							v
							, Class.forName(ptype.getTypeName())));
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
	
	private static <T> List<T> toArrayPojo(JsonHub jh, Class<T> classOfT)
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
				
				inst.add(classOfT.cast(toArrayPojo(v, classOfT)));
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
