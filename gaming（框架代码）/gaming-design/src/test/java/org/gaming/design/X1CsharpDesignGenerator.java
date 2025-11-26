/**
 * 
 */
package org.gaming.design;

import org.gaming.design.export.CsvReader;
import org.gaming.design.gen.csharp.CSharpDesignTemplate;
import org.gaming.design.meta.ExportType;

/**
 * @author YY
 *
 */
public class X1CsharpDesignGenerator {

	public static void main(String[] args) {
		String fileLocation = "E:\\Workspace\\Git\\xgcj-unity\\Assets\\Scripts\\Config\\AutoGen";
		String exportSourcePath = "E:\\Workspace\\Git\\xgcj-document\\docs\\export\\client-csv";
		String genFilePath = "E:\\Workspace\\Git\\gaming\\gaming-design\\gen-template\\csharp-gen.txt";
		
		CSharpDesignTemplate template = new CSharpDesignTemplate(genFilePath);
		template.gen(fileLocation, exportSourcePath, new CsvReader(), ExportType.BOTH);
	}
}
