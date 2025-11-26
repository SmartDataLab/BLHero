/**
 * 
 */
package com.xiugou.x1.excel.export;

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
		
		genCode(fileLocation, exportSourcePath, genFilePath);
	}
	
	public static void genCode(String genOutputPath, String configInputPath, String genFilePath) {
		JavaDesignTemplate template = new JavaDesignTemplate(genFilePath);
		template.gen(genOutputPath, configInputPath, new CsvReader(), ExportType.S);
	}
}
