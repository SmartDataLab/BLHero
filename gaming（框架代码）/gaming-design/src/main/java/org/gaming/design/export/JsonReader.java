/**
 * 
 */
package org.gaming.design.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.gaming.design.loader.DesignFile;
import org.gaming.design.loader.IDesignReader;
import org.gaming.tool.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YY
 *
 */
public class JsonReader implements IDesignReader {

	private static Logger logger = LoggerFactory.getLogger(JsonReader.class);
	
	@Override
	public List<DesignFile> read(File file) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			StringBuilder builder = new StringBuilder();
			
			String line = reader.readLine();
			while(line != null) {
				builder.append(line);
				line = reader.readLine();
			}
			
			DesignFile designFile = GsonUtil.parseJson(builder.toString(), DesignFile.class);
			
			//把className还原出来
			designFile.fillMacro(designFile.getMacros());
			String fileName = file.getName().substring(0, file.getName().indexOf("."));
			designFile.setName(fileName);
            return Collections.singletonList(designFile);
		} catch(Exception e) {
			logger.error(String.format("%s loading config error! ", file.getName()), e);
            return Collections.emptyList();
		}
	}

}
