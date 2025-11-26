/**
 * 
 */
package org.gaming.design.gen.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author YY
 *
 */
public class JavaGen {
	private String classname;
	private String packge;
	private List<String> formats;
	
	private JavaGen() {}
	
	public static JavaGen readGenFile(String genFile) {
		File file = new File(genFile);
		try {
			String classname = null;
			String packge = null;
			List<String> formats = new ArrayList<>();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = reader.readLine();
			
			while(line != null) {
				if(line.startsWith("classname")) {
					classname = line.replace("classname", "").trim();
				} else if(line.startsWith("package")) {
					packge = line.trim();
				} else if(line.startsWith("format")) {
					formats.add(line.replace("format", "").trim());
				}
				line = reader.readLine();
			}
			
			reader.close();
			
			JavaGen gen = new JavaGen();
			gen.classname = classname;
			gen.packge = packge;
			gen.formats = Collections.unmodifiableList(formats);
			return gen;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected String getPackge() {
		return packge;
	}

	protected List<String> getFormats() {
		return formats;
	}

	protected String getClassname() {
		return classname;
	}
}
