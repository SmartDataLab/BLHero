/**
 * 
 */
package pojo.xiugou.x1.pojo.apiutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.gaming.tool.MD5Util;

/**
 * @author YY
 * 37互娱的Api接口验签工具
 */
public class ApiUtil {
	/**
	 * 将各个参数先根据键的排序后，按照（键valueSeperator值pairSeperator键valueSeperator值pairSeperator...）的方式拼接起来
	 * @param signParams
	 * @param signKey
	 * @param valueSeperator key与value之间的分隔符
	 * @param pairSeperator 键值对之间的分隔符
	 * @return
	 */
	public static String jointKeyValueToSource(Map<String, Object> signParams, String signKey, String valueSeperator, String pairSeperator) {
		List<String> keyList = new ArrayList<>(signParams.keySet());
		Collections.sort(keyList);
		StringBuilder builder = new StringBuilder();
		for(String key : keyList) {
			builder.append(key).append(valueSeperator).append(signParams.get(key).toString()).append(pairSeperator);
		}
		builder.append(signKey);
		return builder.toString();
	}
	
	/**
	 * 将各个参数先根据键的排序后，按照（值值...）的方式拼接起来
	 * @param signMap
	 * @return
	 */
	public static String jointValueToSource(Map<String, Object> signParams, String signKey) {
		List<String> keyList = new ArrayList<>(signParams.keySet());
		Collections.sort(keyList);
		StringBuilder builder = new StringBuilder();
		for(String key : keyList) {
			builder.append(signParams.get(key));
		}
		builder.append(signKey);
		return builder.toString();
	}
	
	/**
	 * 将各个参数按照（值值...）的方式拼接起来
	 * @param signParams
	 * @param signKey
	 * @return
	 */
	public static String jointValueToSource(List<String> signParams, String signKey) {
		StringBuilder builder = new StringBuilder();
		for(String value : signParams) {
			builder.append(value);
		}
		builder.append(signKey);
		return builder.toString();
	}
	
	/**
	 * 根据源字符串构建MD5签名
	 * @param source
	 * @return
	 */
	public static String buildSign(String source) {
		return MD5Util.getMD5(source);
	}
}
