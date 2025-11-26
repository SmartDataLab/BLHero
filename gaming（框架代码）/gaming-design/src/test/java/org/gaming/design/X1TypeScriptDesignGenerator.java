/**
 * 
 */
package org.gaming.design;

import org.gaming.design.export.CsvReader;
import org.gaming.design.gen.typescript.TypeScriptDesignTemplate;
import org.gaming.design.meta.ExportType;

/**
 * @author YY
 *
 */
public class X1TypeScriptDesignGenerator {

	public static void main(String[] args) {
		String fileLocation = "E:\\Workspace\\Svn\\x1doc\\config\\export\\ts-gen";
		String exportSourcePath = "E:\\Workspace\\Svn\\x1doc\\config\\export\\client-csv";
		String genFilePath = "E:\\Workspace\\Git\\gaming\\gaming-design\\gen-template\\ts-gen.txt";

		TypeScriptDesignTemplate template = new TypeScriptDesignTemplate(genFilePath);
		template.gen(fileLocation, exportSourcePath, new CsvReader(), ExportType.BOTH);
	}
}
