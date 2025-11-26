/**
 * 
 */
package org.gaming.ruler.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.gaming.tool.FileUtil;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author YY
 *
 */
public class HttpUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	private static final int TIME_OUT = 5;
	
	
	public static String get(String httpUrl) {
		return get(httpUrl, null, null);
	}
	
	private static String readFrom(HttpURLConnection httpCon) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			
			InputStream in = httpCon.getInputStream();
			byte[] readByte = new byte[1024];
			// 读取返回的内容
			int readCount = in.read(readByte, 0, readByte.length);
			while (readCount != -1) {
				baos.write(readByte, 0, readCount);
				readCount = in.read(readByte, 0, readByte.length);
			}
			return new String(baos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 通过HTTP GET 发送参数
	 * 
	 * @param httpUrl
	 * @param parameter
	 */
	public static String get(String httpUrl, Map<String, ?> parameter) {
		return get(httpUrl, parameter, null);
	}
	
	/**
	 * 通过HTTP GET 发送参数
	 * 
	 * @param httpUrl
	 * @param parameter
	 * @param headerMap
	 */
	public static String get(String httpUrl, Map<String, ?> parameter, Map<String, String> headerMap) {
		if (httpUrl == null) {
			return null;
		}
		String urlStr = httpUrl;
		if(parameter != null) {
			urlStr = httpUrl + '?' + buildFormData(parameter);
		}
		
		HttpURLConnection httpCon = null;
		try {
			URL url = new URL(urlStr);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(TIME_OUT * 1000);
			httpCon.setUseCaches(false);
			httpCon.setRequestMethod("GET");
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					httpCon.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			return readFrom(httpCon);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpCon != null) {
				httpCon.disconnect();
			}
		}
		return null;
	}
	
	private static String buildFormData(Map<String, ?> parameter) {
		StringBuilder builder = new StringBuilder();
		if(parameter == null) {
			return builder.toString();
		}
		boolean first = true;
		for(Entry<String, ?> entry : parameter.entrySet()) {
			if(!first) {
				builder.append("&");
			}
			String value;
			try {
				value = URLEncoder.encode(entry.getValue().toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				value = "";
			}
			builder.append(entry.getKey()).append('=').append(value);
			first = false;
		}
		return builder.toString();
	}

	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody) {
		return sentPost(httpUrl, postBody, "UTF-8", null);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 */
	public static String sentPost(String httpUrl, String postBody, String encoding) {
		return sentPost(httpUrl, postBody, encoding, null);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * @param httpUrl   目的地址
	 * @param postBody  post的包体
	 * @param headerMap 增加的Http头信息
	 * @return
	 */
	public static String sentPost(String httpUrl, String postBody, Map<String, String> headerMap) {
		return sentPost(httpUrl, postBody, "UTF-8", headerMap);
	}

	/**
	 * 使用HTTP POST 发送文本
	 * 
	 * @param httpUrl
	 *            发送的地址
	 * @param postBody
	 *            发送的内容
	 * @param encoding
	 *            发送的内容的编码
	 * @param headerMap 增加的Http头信息          
	 * @return 返回HTTP SERVER的处理结果,如果返回null,发送失败
	 * .................
	 */
	public static String sentPost(String httpUrl, String postBody, String encoding, Map<String, String> headerMap) {
		HttpURLConnection httpCon = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(TIME_OUT * 1000);
			httpCon.setUseCaches(false);
			httpCon.setRequestMethod("POST");
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					httpCon.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			OutputStream output = httpCon.getOutputStream();
			output.write(postBody.getBytes(encoding));
			output.flush();
			output.close();
			return readFrom(httpCon);
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpCon != null) {
				httpCon.disconnect();
			}
		}
		return null;
	}
	
	public static String formPost(String httpUrl, Map<String, ?> parameter) {
		return formPost(httpUrl, parameter, "UTF-8", null);
	}
	
	public static String formPost(String httpUrl, Map<String, ?> parameter, String encoding, Map<String, String> headerMap) {
		String formData = buildFormData(parameter);
		HttpURLConnection httpCon = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(TIME_OUT * 1000);
			httpCon.setUseCaches(false);
			httpCon.setRequestMethod("POST");
			if (headerMap != null) {
				for (Entry<String, ?> entry : headerMap.entrySet()) {
					httpCon.addRequestProperty(entry.getKey(), entry.getValue().toString());
				}
			}
			OutputStream output = httpCon.getOutputStream();
			output.write(formData.getBytes(encoding));
			output.flush();
			output.close();
			return readFrom(httpCon);
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpCon != null) {
				httpCon.disconnect();
			}
		}
		return null;
	}
	
	public static String jsonPost(String httpUrl, Object object) {
		return jsonPost(httpUrl, object, "UTF-8", null);
	}
	
	public static String jsonPost(String httpUrl, Object object, String encoding, Map<String, String> headerMap) {
		HttpURLConnection httpCon = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(TIME_OUT * 1000);
			httpCon.setUseCaches(false);
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty("Content-Type", "application/json");
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					httpCon.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			OutputStream output = httpCon.getOutputStream();
			String json = GsonUtil.toJson(object);
			output.write(json.getBytes(encoding));
			output.flush();
			output.close();
			return readFrom(httpCon);
		} catch (Exception e) {
			logger.error(String.format("请求%s发生异常", httpUrl), e);
		} finally {
			if (httpCon != null) {
				httpCon.disconnect();
			}
		}
		return null;
	}
	
	public static String postParamAndFile(String httpUrl, Map<String, ?> params, List<File> files) {
		return postParamAndFile(httpUrl, params, files, "UTF-8");
	}
	public static String postParamAndFile(String httpUrl, Map<String, ?> params, List<File> files, String charset) {
		String boundary = UUID.randomUUID().toString().replaceAll("-", "");
		String prefix = "--";
		String newline = "\r\n";
		
		HttpURLConnection httpCon = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
			httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);
			httpCon.setConnectTimeout(TIME_OUT * 1000);
			httpCon.setReadTimeout(TIME_OUT * 1000);
			httpCon.setUseCaches(false);
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//			if (headerMap != null) {
//				for (Entry<String, String> entry : headerMap.entrySet()) {
//					httpCon.addRequestProperty(entry.getKey(), entry.getValue());
//				}
//			}
			DataOutputStream output = new DataOutputStream(httpCon.getOutputStream());
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				String key = entry.getKey();
				Object value = params.get(key);
				output.writeBytes(prefix + boundary + newline);
				output.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + newline);
				output.writeBytes(newline);
				output.write(value.toString().getBytes(charset));
				output.writeBytes(newline);
			}
			
			for(File file : files) {
				byte[] fileData = FileUtil.readFile(file);
				
				output.writeBytes(prefix + boundary + newline);
				String fileDis = "Content-Disposition: form-data; name=\"files\"" + "; filename=\"" + file.getName() + "\"" + newline;
				output.write(fileDis.getBytes(charset));
				output.writeBytes(newline);
				output.write(fileData);
				output.writeBytes(newline);
			}
			output.writeBytes(prefix + boundary + prefix + newline);
			
			output.flush();
			output.close();
			return readFrom(httpCon);
		} catch (Exception e) {
			logger.error(String.format("请求%s发生异常", httpUrl), e);
		} finally {
			if (httpCon != null) {
				httpCon.disconnect();
			}
		}
		return null;
	}
	
	
	public static String getRemoteIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
	}
	
	
	public static void main(String[] args) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("serverUids", "10004,10007");
		List<File> files = new ArrayList<>();
		File file = new File("E:\\Workspace\\Svn\\x1\\doc\\config\\export\\server-csv\\1道具表_Item.csv");
		files.add(file);
		String response = postParamAndFile("http://127.0.0.1:9001/fixDesign/upload.gate", map, files, "UTF-8");
		
		System.out.println(response);
		
//		Map<String, Object> map = new HashMap<>();
//		map.put("id", 7656);
//		map.put("type", "1");
//		map.put("title", "系统有点斤斤计较");
//		map.put("content", "系统有点斤斤计较驱蚊器翁");
////		map.put("roleIds", "[100001,100002,100003]");
////		map.put("goods", "[{\"item_type\":100001,\"item_num\":99},{\"item_type\":100002,\"item_num\":99}]");
////		http://127.0.0.1:20004/mailApi/deleteMailSystem?mailSystemId=4856
//		String res1 = HttpUtil.jsonPost("http://127.0.0.1:20004/mailApi/sendMailSystem", map);
//		System.out.println(res1);
//		String res2 = HttpUtil.formPost("http://127.0.0.1:20004/mailApi/sendMailSystem2", map);
//		System.out.println(res2);
//		String sss = HttpUtil.get("http://192.168.1.33:9001/login.gate?username=yy&password=123");
//		System.out.println(sss);
	}
}
