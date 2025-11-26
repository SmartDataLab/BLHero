/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal.BooleanList;
import com.google.protobuf.Internal.IntList;
import com.google.protobuf.Internal.LongList;
import com.google.protobuf.LazyStringList;
import com.google.protobuf.Parser;
import com.google.protobuf.RepeatedFieldBuilderV3;
import com.google.protobuf.SingleFieldBuilderV3;

/**
 * @author YY
 *
 */
public class ProtocolPrinter {

	public static Map<Class<?>, ProtocolClassPrinter> classPrinterMap = new HashMap<>();

	public static String print(Object object) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("{");
			printObject(object, "", builder, false);
			builder.append("\n}");
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private static final String SEPARATOR = "    ";
	// private static final String SEPARATOR = "\t";

	@SuppressWarnings("rawtypes")
	public static void doPrint(Object object, String offset, StringBuilder result, boolean printInline) {
		if (object instanceof String) {
			result.append(object.toString());
		} else if (object instanceof List) {
			result.append("[");
			List list = (List) object;
			if (list.size() > 0) {
				result.append("\n" + offset + SEPARATOR);
			}
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				if (i == list.size() - 1) {
					doPrint(obj, offset, result, true);
				} else {
					doPrint(obj, offset, result, true);
					result.append(",");
					result.append("\n" + offset + SEPARATOR);
				}
			}
			result.append("]");
		} else if (object instanceof AbstractMessageLite) {
			Class<?> objClass = object.getClass();
			result.append("{");

			ProtocolClassPrinter printer = classPrinterMap.get(objClass);
			if (printer != null) {
				printer.print(object, offset, result, false);
			} else {
				printObject(object, offset, result, printInline);
			}
			result.append("}");
		} else {
			result.append(object.toString());
		}
	}
	
	public static void printObject(Object object, String offset, StringBuilder result, boolean printInline) {
		Class<?> objClass = object.getClass();
		
		Class<?> builderClass = null;
		for (Class<?> cls : objClass.getDeclaredClasses()) {
			if ("Builder".equals(cls.getSimpleName())) {
				builderClass = cls;
				break;
			}
		}
		try {
			boolean isFirstField = true;

			for (Field field : builderClass.getDeclaredFields()) {
				if (Pattern.matches("bitField[0-9]*_", field.getName())) {
					continue;
				}
				Method getter = printGetter(objClass, field);
				if (getter == null) {
					continue;
				}
				Method hasser = printHasser(objClass, field);
				if (hasser != null) {
					Object hasValue = hasser.invoke(object);
					if (!hasValue.equals(Boolean.TRUE)) {
						continue;
					}
				}
				if (!isFirstField) {
					result.append(", ");
				}
				Object value = getter.invoke(object);
				if (printInline) {
					result.append(field.getName().replaceAll("_", "") + ":");
				} else {
					result.append("\n" + offset + SEPARATOR + field.getName().replaceAll("_", "") + ":");
				}
				doPrint(value, offset + SEPARATOR, result, printInline);
				isFirstField = false;
			}
		} catch (Exception e) {
		}
	}
	

	private static Method printGetter(Class<?> objClass, Field field) {
		Method getter = null;
		try {
			String fieldName = field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1).replace("_", "");
			if (String.class.isAssignableFrom(field.getType())) {
				getter = objClass.getDeclaredMethod("get" + fieldName, new Class[0]);
			} else if (List.class.isAssignableFrom(field.getType())) {
				getter = objClass.getDeclaredMethod("get" + fieldName + "List", new Class[0]);
			} else if (LazyStringList.class.isAssignableFrom(field.getType())) {
				getter = objClass.getDeclaredMethod("get" + fieldName + "List", new Class[0]);
			} else if (RepeatedFieldBuilderV3.class.isAssignableFrom(field.getType())
					|| SingleFieldBuilderV3.class.isAssignableFrom(field.getType())) {

			} else {
				getter = objClass.getDeclaredMethod("get" + fieldName, new Class[0]);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return getter;
	}

	private static Method printHasser(Class<?> objClass, Field field) {
		Method hasser = null;
		try {
			String fieldName = field.getName().substring(0, 1).toUpperCase()
					+ field.getName().substring(1).replace("_", "");
			if (String.class.isAssignableFrom(field.getType())) {

			} else if (List.class.isAssignableFrom(field.getType())) {

			} else if (LazyStringList.class.isAssignableFrom(field.getType())) {

			} else if (RepeatedFieldBuilderV3.class.isAssignableFrom(field.getType())
					|| SingleFieldBuilderV3.class.isAssignableFrom(field.getType())) {

			} else if (GeneratedMessageV3.class.isAssignableFrom(field.getType())) {
				hasser = objClass.getDeclaredMethod("has" + fieldName, new Class[0]);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return hasser;
	}

	public static String print(Class<?> clazz) {
		Class<?> buildClass = null;
		for (Class<?> cls : clazz.getDeclaredClasses()) {
			if ("Builder".equals(cls.getSimpleName())) {
				buildClass = cls;
				break;
			}
		}
		StringBuilder result = new StringBuilder();
		result.append("{");
		print0(buildClass, result, false);
		result.append("\n}");
		return result.toString();
	}

	private static void print0(Class<?> clazz, StringBuilder result, boolean isInArray) {
		List<Field> fields = new ArrayList<>();
		// 先过滤出有效的字段
		for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
			Field field = clazz.getDeclaredFields()[i];
			if (Pattern.matches("bitField[0-9]*_", field.getName())
					|| SingleFieldBuilderV3.class.equals(field.getType())
					|| RepeatedFieldBuilderV3.class.equals(field.getType()) || Parser.class.equals(field.getType())
					|| "DEFAULT_INSTANCE".equals(field.getName()) || field.getName().endsWith("_FIELD_NUMBER")
					|| field.getName().equals("serialVersionUID") || field.getName().equals("memoizedIsInitialized")
					|| field.getName().equals("intsMemoizedSerializedSize")
					|| field.getName().equals("longsMemoizedSerializedSize")) {
				continue;
			}
			fields.add(field);
		}
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (isInArray) {
				if (i >= fields.size() - 1) {
					result.append(field.getName().replaceAll("_", "") + ": ");
				} else {
					result.append(field.getName().replaceAll("_", "") + ": ");
				}
			} else {
				result.append("\n" + field.getName().replaceAll("_", "") + ":");
			}
			if (AbstractMessageLite.class.isAssignableFrom(field.getType())) {
				print0(field.getType(), result, isInArray);
			} else if (LazyStringList.class.isAssignableFrom(field.getType())) {
				result.append("[,...]");
			} else if (IntList.class.isAssignableFrom(field.getType())) {
				result.append("[,...]");
			} else if (LongList.class.isAssignableFrom(field.getType())) {
				result.append("[,...]");
			} else if (BooleanList.class.isAssignableFrom(field.getType())) {
				result.append("[,...]");
			} else if (List.class.isAssignableFrom(field.getType())) {
				ParameterizedType pt = (ParameterizedType) field.getGenericType();
				Class<?> listClazz = (Class<?>) pt.getActualTypeArguments()[0];
				if (Number.class.isAssignableFrom(listClazz) || String.class.isAssignableFrom(listClazz)) {
					result.append("[,...]");
				} else {
					result.append("[{");
					print0(listClazz, result, true);
					result.append("},...]");
				}
			}
			if (i < fields.size() - 1) {
				result.append(",");
			}
		}
	}
}
