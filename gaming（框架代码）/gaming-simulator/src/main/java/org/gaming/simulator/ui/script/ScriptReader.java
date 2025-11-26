/**
 * 
 */
package org.gaming.simulator.ui.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class ScriptReader {
	
	public static List<String> readScript(String filePath) {
		List<String> content = new ArrayList<>();
		try {
			File file = new File(filePath);
			if(file.exists()) {
				FileReader fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
				
				String line = reader.readLine();
				while(line != null) {
					content.add(line);
					line = reader.readLine();
				}
				reader.close();
				fr.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static void saveScript(String filePath, List<String> content) {
		File file = new File(filePath);
		
		try {
			FileWriter fw = new FileWriter(file);
			
			for(int i = 0; i < content.size(); i++) {
				String str = content.get(i);
				if(i == 0) {
					fw.write(str + "\n");
				} else {
					fw.append(str + "\n");
				}
			}
			fw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
