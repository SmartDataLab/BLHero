/**
 * 
 */
package org.gaming.design.gen.typescript;

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
public class TypeScriptGen {
	private String classname;
	private List<String> imports;
	private List<String> formats;
	private List<String> customs;
	
	public static TypeScriptGen readGenFile(String genFile) {
		File file = new File(genFile);
		try {
			String classname = null;
			List<String> imports = new ArrayList<>();
			List<String> formats = new ArrayList<>();
			List<String> customs = new ArrayList<>();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = reader.readLine();
			
			while(line != null) {
				if(line.startsWith("classname")) {
					classname = line.replace("classname", "").trim();
				} else if(line.startsWith("import")) {
					imports.add(line.trim());
				} else if(line.startsWith("format")) {
					formats.add(line.replace("format", "").trim());
				} else if(line.startsWith("#")) {
					customs.add(line.replace("#", ""));
				}
				line = reader.readLine();
			}
			
			reader.close();
			
			TypeScriptGen gen = new TypeScriptGen();
			gen.classname = classname;
			gen.imports = Collections.unmodifiableList(imports);
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

	protected List<String> getCustoms() {
		return customs;
	}

	protected List<String> getImports() {
		return imports;
	}

	protected List<String> getFormats() {
		return formats;
	}

	protected String getClassname() {
		return classname;
	}
}
