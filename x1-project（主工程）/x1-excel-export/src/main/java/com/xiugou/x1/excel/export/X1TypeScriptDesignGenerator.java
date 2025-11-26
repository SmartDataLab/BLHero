/**
 * 
 */
package com.xiugou.x1.excel.export;

import org.gaming.design.export.CsvReader;
import org.gaming.design.gen.typescript.TypeScriptDesignTemplate;
import org.gaming.design.meta.ExportType;

/**
 * @author YY
 *
 */
public class X1TypeScriptDesignGenerator {

	public static void main(String[] args) {
		String genOutputPath = "E:\\ggg";
		String configInputPath = "E:\\Workspace\\Svn\\x1doc\\config\\export\\client-csv";
		String genFilePath = "E:\\Workspace\\Svn\\x1doc\\config\\ts-gen.txt";
		
		genCode(genOutputPath, configInputPath, genFilePath);
	}

	public static void genCode(String genOutputPath, String configInputPath, String genFilePath) {
		TypeScriptDesignTemplate template = new TypeScriptDesignTemplate(genFilePath);
		template.gen(genOutputPath, configInputPath, new CsvReader(), ExportType.C);
	}
}
