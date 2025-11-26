/**
 * 
 */
package org.gaming.tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author YY
 *
 */
public class ConsoleUtil {

	private static Map<String, ConsoleInvoker> functionMap = new HashMap<>();
	
	/**
     * 在windows系统上运行的时候，用命令来关闭
     */
    public static void inputListening() {
        try {
        	Properties properties = System.getProperties();
        	if(!properties.get("os.name").toString().contains("Windows") && !properties.get("os.name").toString().contains("Mac OS X")) {
        		return;
        	}
        	
        	InputStream is = System.in;
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        	while(true) {
        		try {
        			String inputStr = br.readLine();
        			if("stop".equals(inputStr)) {
        				System.exit(0);
        			} else {
        				ConsoleInvoker func = functionMap.get(inputStr);
        				if(func != null) {
        					func.function.apply();
        				}
        			}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void addFunction(String input, ConsoleFunction func) {
    	StackTraceElement element = new RuntimeException().getStackTrace()[1];
    	ConsoleInvoker invoker = new ConsoleInvoker();
    	invoker.function = func;
    	invoker.className = element.getClassName();
    	functionMap.put(input, invoker);
    }
    
    public static interface ConsoleFunction {
    	void apply();
    }
    
    private static class ConsoleInvoker {
    	private String className;
    	private ConsoleFunction function;
    }
    
    public static void printFunction() {
    	for(Entry<String, ConsoleInvoker> entry : functionMap.entrySet()) {
    		System.out.println(entry.getKey() + "\t" + entry.getValue().className);
    	}
    }
}
