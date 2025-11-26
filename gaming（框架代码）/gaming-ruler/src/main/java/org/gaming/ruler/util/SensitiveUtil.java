/**
 * 
 */
package org.gaming.ruler.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class SensitiveUtil {

	static Logger logger = LoggerFactory.getLogger(SensitiveUtil.class);

	/**
	 * 敏感词缓存Map(第一个字符，其余字符的List)
	 */
	private static Map<String, Set<String>> sensitiveMap;
	/**
	 * 敏感词缓存Map<第一个字符，其余字符的List>，List已经过排序与去重，字符越长排得越前
	 */
	private static Map<String, List<String>> sensitiveListMap;
	/**
	 * 过滤字符缓存
	 */
	private static Map<Integer, String> filterCharMap = new ConcurrentHashMap<>();
	/**
	 * 替换的字符
	 */
	private static final String FILTER_CHAR = "*";
	/**
	 * 过滤的匹配规则
	 */
	private static List<Pattern> filterPatterns;
	
	public static void main(String[] args) {
		SensitiveBuilder builder = new SensitiveBuilder();
		builder.addSensitive("A");
		builder.addSensitive("AB");
		builder.addSensitive("ABC");
		builder.addSensitive("ABCD");
		
		builder.addSensitive("习");
		builder.addSensitive("习近");
		builder.addSensitive("习近平");
		builder.addSensitive("习近平A");
		builder.setUp();
		
		String result = SensitiveUtil.filter("ABCD 习近平");
		System.out.println(result);
		result = SensitiveUtil.has("ABCD 习近平");
		System.out.println(result);
	}
	
	/**
	 * 敏感字过滤，优先匹配长的词语
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		str = str.trim();
		String[] strs = str.split("");
		for(int index = 0; index < strs.length; index++) {
			String temp = strs[index];
			List<String> words = sensitiveListMap.get(temp);
			if(words == null) {
				continue;
			}
			int j = index + 1;
			for (String word : words) {
				if (j + word.length() > strs.length) {
					continue;
				}
				if (word.equals(str.substring(j, j + word.length()))) {
					str = str.replaceAll(temp + word, getFilter(temp + word));
					j += word.length();
					break;
				}
			}
		}
		return str;
	}
	
	/**
	 * 是否有敏感词，存在敏感字则返回首个敏感词，优先匹配长的词语
	 * @param str
	 * @return
	 */
	public static String has(String str) {
		str = str.trim();
		String[] strs = str.split("");
		for(int index = 0; index < strs.length; index++) {
			String temp = strs[index];
			List<String> words = sensitiveListMap.get(temp);
			if(words == null) {
				continue;
			}
			int j = index + 1;
			for (String word : words) {
				if (j + word.length() > strs.length) {
					continue;
				}
				if (word.equals(str.substring(j, j + word.length()))) {
					return temp + word;
				}
			}
		}
		return null;
	}
	
	/**
	 * 是否有敏感词，存在敏感字则返回首个敏感词
	 * 
	 * @param str
	 * @return
	 */
	public static String hasSensitive(String str) {
		str = str.trim();
		String[] strs = str.split("");
		int index = 0;
		while (index < strs.length) {
			String temp = strs[index];
			Set<String> map = sensitiveMap.get(temp);
			if (map == null || map.isEmpty()) {
				index++;
			} else {
				int j = index + 1;
				for (String key : map) {
					if (j + key.length() > strs.length) {
						continue;
					}
					if (j + key.length() > str.length()) {
						continue;
					}
					if (key.equals(str.substring(j, j + key.length()))) {
						return temp + key;
					}
				}
				index++;
			}
		}
		return null;
	}
	
	
	/**
	 * 敏感字过滤
	 * 
	 * @param str
	 * @return
	 */
	public static String sensitiveFilter(String str) {
		str = str.trim();
		String[] strs = str.split("");
		int index = 0;
		while (index < strs.length) {
			String temp = strs[index];
			Set<String> words = sensitiveMap.get(temp);
			if (words == null) {
				index++;
			} else {
				int j = index + 1;
				for (String word : words) {
					if (j + word.length() > strs.length) {
						continue;
					}
					if (j + word.length() > str.length()) {
						continue;
					}
					if (word.equals(str.substring(j, j + word.length()))) {
						str = str.replaceAll(temp + word, getFilter(temp + word));
						j += word.length();
						break;
					}
				}
				index++;
			}
		}
		return str;
	}

	/**
	 * 组装屏蔽字符
	 * 
	 * @param word
	 * @return
	 */
	private static String getFilter(String word) {
		String filter = filterCharMap.get(word.length());
		if (filter == null) {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < word.length(); i++) {
				builder.append(FILTER_CHAR);
			}
			filterCharMap.put(word.length(), builder.toString());
			//不要用map.put返回的结果赋值给filter，因为会为null
			//这里也不需要考虑map中的对象跟filter的对象是否是同一个，即便不是同一个，其结果都是一样的
			filter = builder.toString();
		}
		return filter;
	}
	
	public static SensitiveResult matchAnyPattern(String content) {
		if(filterPatterns == null) {
			return new SensitiveResult(false, "");
		}
		for(Pattern pattern : filterPatterns) {
			if(pattern.matcher(content).find()) {
				return new SensitiveResult(true, pattern.pattern());
			}
		}
		return new SensitiveResult(false, "");
	}
	
	public static interface ISensitiveWord {
		String getWord();
	}
	
	public static SensitiveBuilder newBuilder() {
		return new SensitiveBuilder();
	}
	
	public static class SensitiveBuilder {
		/**
		 * 使用Set可以起到去重的效果
		 */
		private Map<String, Set<String>> sensitiveMap;
		private List<Pattern> filterPatterns;
		private SensitiveBuilder() {}
		/**
		 * 添加敏感词
		 * @param words
		 * @return
		 */
		public SensitiveBuilder addSensitive(String... words) {
			if(sensitiveMap == null) {
				sensitiveMap = new HashMap<>();
			}
			for (int i = 0; i < words.length; i++) {
				String word = words[i].trim();
				if (word == null || "".equals(word)) {
					continue;
				}
				String firstChar = word.substring(0, 1);
				Set<String> wordSet = sensitiveMap.get(firstChar);
				if (wordSet == null) {
					wordSet = new HashSet<>();
					sensitiveMap.put(firstChar, wordSet);
				}
				wordSet.add(word.substring(1));
			}
			return this;
		}
		/**
		 * 添加敏感词
		 * @param words
		 * @return
		 */
		public SensitiveBuilder addSensitive(Collection<String> words) {
			String[] array = new String[words.size()];
			int index = 0;
			for(String word : words) {
				array[index] = word;
				index++;
			}
			return addSensitive(array);
		}
		/**
		 * 添加敏感词
		 * @param words
		 * @return
		 */
		public SensitiveBuilder addSensitiveWords(Collection<? extends ISensitiveWord> words) {
			String[] array = new String[words.size()];
			int index = 0;
			for(ISensitiveWord word : words) {
				array[index] = word.getWord();
				index++;
			}
			return addSensitive(array);
		}
		public SensitiveBuilder addStringWords(Collection<String> words) {
			String[] array = new String[words.size()];
			int index = 0;
			for(String word : words) {
				array[index] = word;
				index++;
			}
			return addSensitive(array);
		}
		/**
		 * 添加匹配规则
		 * @param pattern
		 * @return
		 */
		public SensitiveBuilder addPattern(Pattern pattern) {
			if(filterPatterns == null) {
				filterPatterns = new ArrayList<>();
			}
			filterPatterns.add(pattern);
			return this;
		}
		
		public void setUp() {
			if(sensitiveMap != null) {
				SensitiveUtil.sensitiveMap = sensitiveMap;
				Map<String, List<String>> sensitiveListMap = new HashMap<>();
				for(Entry<String, Set<String>> entry : sensitiveMap.entrySet()) {
					List<String> list = new ArrayList<>(entry.getValue());
					list.sort(LENGTH_SORT);
					sensitiveListMap.put(entry.getKey(), list);
				}
				SensitiveUtil.sensitiveListMap = sensitiveListMap;
			}
			if(filterPatterns != null) {
				SensitiveUtil.filterPatterns = filterPatterns;
			}
		}
	}
	
	public static Comparator<String> LENGTH_SORT = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return Integer.compare(o2.length(), o1.length());
		}
	};
	
	public static class SensitiveResult {
		/**
		 * true包含有敏感信息
		 */
		private final boolean matchAnyPattern;
		private final String pattern;
		public SensitiveResult(boolean matchAnyPattern, String pattern) {
			this.matchAnyPattern = matchAnyPattern;
			this.pattern = pattern;
		}
		public boolean isMatchAnyPattern() {
			return matchAnyPattern;
		}
		public String getPattern() {
			return pattern;
		}
	}
}
