/**
 * 
 */
package org.gaming.design;

import org.gaming.design.export.CsvReader;
import org.gaming.design.gen.java.JavaDesignTemplate;
import org.gaming.design.meta.ExportType;

/**
 * @author YY
 *
 */
public class X1JavaConfigGenerator {

	public static void main(String[] args) {
		String fileLocation = "E:\\Workspace\\Git\\x1-server\\x1-project\\x1-design\\src\\main\\java\\com\\xiugou\\x1\\design\\module";
		String exportSourcePath = "E:\\Workspace\\Svn\\x1doc\\config\\export\\server-csv";
		String genFilePath = "E:\\Workspace\\Git\\gaming\\gaming-design\\gen-template\\java-gen.txt";
		
		JavaDesignTemplate template = new JavaDesignTemplate(genFilePath);
		template.gen(fileLocation, exportSourcePath, new CsvReader(), ExportType.BOTH);
	}
}
