/**
 * 
 */
package org.gaming.tool;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author YY
 * gson的Expose注解里面有说明指出当对象的属性加上transient关键字时，将不会对其进行序列化或者反序列化
 */
public class GsonUtil {
	
	private static Gson gson = null;
	static {
		gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
	}
	
	private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
		private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		@Override
		public void write(JsonWriter out, LocalDateTime value) throws IOException {
			if(value != null) {
				out.value(value.format(formatter));
			} else {
				out.value("");
			}
		}

		@Override
		public LocalDateTime read(JsonReader in) throws IOException {
			if(in.hasNext()) {
				String nextString = in.nextString();
				if("".equals(nextString.trim())) {
					return LocalDateTime.now();
				} else {
					return LocalDateTime.parse(nextString, formatter);
				}
			} else {
				return LocalDateTime.now();
			}
		}
	}
	
	/**
	 * 将对象转换成json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {
		return gson.toJson(object);
	}

	/**
	 * 通过json字符串解释对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseJson(String json, Class<T> clazz) {
		T t = gson.fromJson(json, clazz);
		if(t == null) {
			try {
				t = clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return t;
	}
	
	public static <T, P> T parseJson(String json, Class<T> clazz, Class<P> parameterClazz) {
		TypeToken<?> token = TypeToken.getParameterized(clazz, parameterClazz);
		T t = gson.fromJson(json, token.getType());
		if(t == null) {
			try {
				t = clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return t;
	}
	
	public static <T> T parseJson(String json, TypeToken<?> token) {
		return gson.fromJson(json, token.getType());
	}
	/**
	 * 通过json获取list
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getList(String json, Class<T> clazz) {
		TypeToken<?> token = TypeToken.getParameterized(List.class, clazz);
		List<T> list = gson.fromJson(json, token.getType());
		if(list == null) {
			list = new ArrayList<>();
		}
		return list;
	}
	
	/**
	 * 通过json获取非线程安全的map
	 * 
	 * @param json
	 * @param keyClazz
	 * @param valueClazz
	 * @return
	 */
	public static <K, V> Map<K, V> getMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
		TypeToken<?> token = TypeToken.getParameterized(HashMap.class, keyClazz, valueClazz);
		Map<K, V> map = gson.fromJson(json, token.getType());
		if(map == null) {
			map = new HashMap<>();
		}
		return map;
	}

	/**
	 * 通过json获取线程安全的map
	 * 
	 * @param json
	 * @param keyClazz
	 * @param valueClazz
	 * @return
	 */
	public static <K, V> ConcurrentMap<K, V> getConcurrentMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
		TypeToken<?> token = TypeToken.getParameterized(ConcurrentMap.class, keyClazz, valueClazz);
		ConcurrentMap<K, V> map = gson.fromJson(json, token.getType());
		if (map == null) {
			map = new ConcurrentHashMap<>();
		}
		return map;
	}
	
	/**
	 * 通过json获取set
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> Set<T> getSet(String json, Class<T> clazz) {
		TypeToken<?> token = TypeToken.getParameterized(Set.class, clazz);
		Set<T> set = gson.fromJson(json, token.getType());
		if(set == null) {
			set = new HashSet<>();
		}
		return set;
	}

	public static Gson getGson() {
		return gson;
	}
	
	public static JsonObject parse(String json) {
		return gson.fromJson(json, JsonObject.class);
	}
	
	public static JsonArray parseArray(String json) {
		return gson.fromJson(json, JsonArray.class);
	}
}
