/**
 * 
 */
package org.gaming.design.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gaming.design.loader.DesignField;
import org.gaming.design.loader.DesignFile;
import org.gaming.design.meta.ExportType;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author YY
 *
 */
public class CsvExporter {
	
	public static void export(Collection<DesignFile> fileMetas, String outputPath, ExportType exportType) {
		for(DesignFile fileMeta : fileMetas) {
			if(!fileMeta.isExport(exportType)) {
				System.out.println("导出忽略----->" + fileMeta.getName());
				continue;
			}
			try {
				List<String[]> allLines = new ArrayList<>();
				allLines.add(fileMeta.getMacros());
				
				List<String> newName = new ArrayList<>();
				List<String> newType = new ArrayList<>();
				List<String> newReadBy = new ArrayList<>();
				List<String> newComment = new ArrayList<>();
				
				for(DesignField field : fileMeta.getFields()) {
					boolean isExport = DesignExporter.isExport(ExportType.vof(field.getReadBy()), exportType);
					if(!isExport) {
						continue;
					}
					newName.add(field.getName());
					newType.add(field.getFieldType());
					newReadBy.add(field.getReadBy());
					newComment.add(field.getComment());
				}
				allLines.add(newName.toArray(new String[newName.size()]));
				allLines.add(newType.toArray(new String[newType.size()]));
				allLines.add(newReadBy.toArray(new String[newReadBy.size()]));
				allLines.add(newComment.toArray(new String[newComment.size()]));
				
				for(String[] data : fileMeta.getDatas()) {
					List<String> newData = new ArrayList<>();
					for(int i = 0; i < fileMeta.getFields().size(); i++) {
						DesignField field = fileMeta.getFields().get(i);
						boolean isExport = DesignExporter.isExport(ExportType.vof(field.getReadBy()), exportType);
						if(!isExport) {
							continue;
						}
						if(i >= data.length) {
							newData.add("");
						} else {
							newData.add(data[i]);
						}
					}
					allLines.add(newData.toArray(new String[newData.size()]));
				}
				
				File file = new File(outputPath + File.separator + fileMeta.getName() + ".csv");
				CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
				csvWriter.writeAll(allLines);
				csvWriter.close();
				System.out.println("导出----->" + fileMeta.getName());
			} catch (Exception e) {
				System.out.println("导出异常----->" + fileMeta.getName());
				e.printStackTrace();
			}
		}
	}
	
	
}
