/**
 * 
 */
package com.xiugou.x1.excel.export;

import org.gaming.design.export.CsvReader;
import org.gaming.design.gen.csharp.CSharpDesignTemplate;
import org.gaming.design.meta.ExportType;

/**
 * @author YY
 *
 */
public class X1CsharpDesignGenerator {

	public static void main(String[] args) {
		String genOutputPath = "E:\\Workspace\\Git\\xgcj-document\\docs\\gen";
		String configInputPath = "E:\\Workspace\\Git\\xgcj-document\\docs\\export\\client-csv";
		String genFilePath = "E:\\Workspace\\Git\\xgcj-document\\docs\\csharp-gen.txt";
		
		genCode(genOutputPath, configInputPath, genFilePath);
	}
	
	public static void genCode(String genOutputPath, String configInputPath, String genFilePath) {
		CSharpDesignTemplate template = new CSharpDesignTemplate(genFilePath);
		template.gen(genOutputPath, configInputPath, new CsvReader(), ExportType.C);
	}
}
