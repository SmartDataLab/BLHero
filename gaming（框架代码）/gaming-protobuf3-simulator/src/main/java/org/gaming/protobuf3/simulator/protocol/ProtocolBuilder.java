/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.gaming.tool.GsonUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Internal.BooleanList;
import com.google.protobuf.Internal.IntList;
import com.google.protobuf.Internal.LongList;
import com.google.protobuf.LazyStringList;

/**
 * @author YY
 *
 */
public class ProtocolBuilder {
	
	public static Object build(Class<? extends AbstractMessage> selectClz, String inputValues) {
		JsonObject jo = GsonUtil.parse(inputValues);
		Object request = doBuild(selectClz, jo);
		return request;
	}
	
	
	private static Object doBuild(Class<?> clazz, JsonObject jo) {
		try {
			Method newMethod = clazz.getDeclaredMethod("newBuilder", new Class[0]);
			Object builder = newMethod.invoke(clazz, new Object[0]);
			Method buildMethod = builder.getClass().getDeclaredMethod("build", new Class[0]);
			
			for(String fieldName : jo.keySet()) {
				JsonElement je = jo.get(fieldName);
				
				Field field = builder.getClass().getDeclaredField(fieldName + "_");
				Method setter = null;
				Class<?> parameterClass = null;
				
				if (LazyStringList.class.isAssignableFrom(field.getType())) {
					parameterClass = String.class;
					setter = builder.getClass().getDeclaredMethod("add" + upperFirst(fieldName),
							getMethodClass(parameterClass));
					for(int i = 0; i < je.getAsJsonArray().size(); i++) {
						String value = je.getAsJsonArray().get(i).getAsString();
						setter.invoke(builder, value);
					}
					
				} else if(IntList.class.isAssignableFrom(field.getType())) {
					parameterClass = Integer.class;
					setter = builder.getClass().getDeclaredMethod("add" + upperFirst(fieldName),
							getMethodClass(parameterClass));
					for(int i = 0; i < je.getAsJsonArray().size(); i++) {
						int value = je.getAsJsonArray().get(i).getAsInt();
						setter.invoke(builder, value);
					}
					
				} else if(LongList.class.isAssignableFrom(field.getType())) {
					parameterClass = Long.class;
					setter = builder.getClass().getDeclaredMethod("add" + upperFirst(fieldName),
							getMethodClass(parameterClass));
					for(int i = 0; i < je.getAsJsonArray().size(); i++) {
						long value = je.getAsJsonArray().get(i).getAsLong();
						setter.invoke(builder, value);
					}
					
				} else if(BooleanList.class.isAssignableFrom(field.getType())) {
					parameterClass = Boolean.class;
					setter = builder.getClass().getDeclaredMethod("add" + upperFirst(fieldName),
							getMethodClass(parameterClass));
					for(int i = 0; i < je.getAsJsonArray().size(); i++) {
						boolean value = je.getAsJsonArray().get(i).getAsBoolean();
						setter.invoke(builder, value);
					}
					
				} else if (List.class.isAssignableFrom(field.getType())) {
					parameterClass = (Class<?>) ((ParameterizedType) field.getGenericType())
							.getActualTypeArguments()[0];
					setter = builder.getClass().getDeclaredMethod("add" + upperFirst(fieldName),
							getMethodClass(parameterClass));
					
					for(int i = 0; i < je.getAsJsonArray().size(); i++) {
						JsonElement jee = je.getAsJsonArray().get(i);
						Object joo = doBuild(parameterClass, jee.getAsJsonObject());
						setter.invoke(builder, joo);
					}
					
				} else if(Object.class.equals(field.getType())) {
					parameterClass = String.class;
					setter = builder.getClass().getDeclaredMethod("set" + upperFirst(fieldName),
							parameterClass);
					
					setter.invoke(builder, je.getAsString());
					
				} else if(int.class.equals(field.getType())) {
					parameterClass = int.class;
					try {
						setter = builder.getClass().getDeclaredMethod("set" + upperFirst(fieldName),
								parameterClass);
					} catch (Exception e) {
						//如果字段是枚举类型，则函数名是setXxxxValue
						setter = builder.getClass().getDeclaredMethod("set" + upperFirst(fieldName) + "Value",
								parameterClass);
					}
					setter.invoke(builder, je.getAsInt());
				
				} else if(long.class.equals(field.getType())) {
					parameterClass = long.class;
					
					setter = builder.getClass().getDeclaredMethod("set" + upperFirst(fieldName),
							parameterClass);
					setter.invoke(builder, je.getAsLong());
				
				} else if(boolean.class.equals(field.getType())) {
					parameterClass = boolean.class;
					
					setter = builder.getClass().getDeclaredMethod("set" + upperFirst(fieldName),
							parameterClass);
					setter.invoke(builder, je.getAsBoolean());
				}
			}
			return buildMethod.invoke(builder, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Class<?> getMethodClass(Class<?> clz) {
		if(clz == Integer.class) {
			return int.class;
		} else if(clz == Long.class) {
			return long.class;
		} else if(clz == Boolean.class) {
			return boolean.class;
		} else {
			return clz;
		}
	}
	
	private static String upperFirst(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
	
	public static void main(String[] args) {
		
//		ProtocolCenter.init();
		
		String str = "{id=1, levelId=2, type=3, num=4 }";
		System.out.println(str.substring(1, str.length() - 1));
		
//		String value = "ssss=[aaa,bbb,ccc]\nbuildings=[{id=1, levelId=2, type=3, num=4 },{id=12, levelId=22, type=32, num=42 }]\niiii=[11,22,33]\niii=5689";
//		Object obj = build("Msg_11001", value);
//		System.out.println(obj);
		
		String json = "{a:111}";
		JsonObject jo = GsonUtil.parse(json);
		System.out.println(jo.get("a"));
		
		String aaaa = "{" + 
				"ints:[1,2]," + 
				"longs:[3,4]," + 
				"strs:[A,B]," + 
				"bools:[true,false]," + 
				"ab:0," + 
				"bc:1," + 
				"cd:444," + 
				"de:true" + 
				"}";
		
		JsonObject jsonObject = GsonUtil.parse(aaaa);
		System.out.println(jsonObject);
	}
}
