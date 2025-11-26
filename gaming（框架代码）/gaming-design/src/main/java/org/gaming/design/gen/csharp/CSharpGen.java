/**
 * 
 */
package org.gaming.design.gen.csharp;

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
public class CSharpGen {
	private String classname;
	private String namespace;
	private List<String> usings;
	private List<String> formats;
	private List<String> customs;
	
	private CSharpGen() {}
	
	public static CSharpGen readGenFile(String genFile) {
		File file = new File(genFile);
		try {
			String classname = null;
			String namespace = null;
			List<String> usings = new ArrayList<>();
			List<String> formats = new ArrayList<>();
			List<String> customs = new ArrayList<>();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = reader.readLine();
			
			while(line != null) {
				if(line.startsWith("classname")) {
					classname = line.replace("classname", "").trim();
				} else if(line.startsWith("namespace")) {
					namespace = line.trim();
				} else if(line.startsWith("using")) {
					usings.add(line.trim());
				} else if(line.startsWith("format")) {
					formats.add(line.replace("format", "").trim());
				} else if(line.startsWith("#")) {
					customs.add(line.replace("#", ""));
				}
				line = reader.readLine();
			}
			
			reader.close();
			
			
			CSharpGen gen = new CSharpGen();
			gen.classname = classname;
			gen.namespace = namespace;
			gen.usings = Collections.unmodifiableList(usings);
			gen.formats = Collections.unmodifiableList(formats);
			gen.customs = Collections.unmodifiableList(customs);
			return gen;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected List<String> getUsings() {
		return usings;
	}

	protected List<String> getFormats() {
		return formats;
	}

	protected List<String> getCustoms() {
		return customs;
	}

	protected String getNamespace() {
		return namespace;
	}

	protected String getClassname() {
		return classname;
	}
}
