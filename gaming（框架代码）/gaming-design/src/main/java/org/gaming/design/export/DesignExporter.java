/**
 * 
 */
package org.gaming.design.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.design.loader.DesignField;
import org.gaming.design.loader.DesignFile;
import org.gaming.design.meta.ExportType;
import org.gaming.tool.FileUtil;

/**
 * @author YY
 *
 */
public class DesignExporter {

	public static Collection<DesignFile> export(String inputPath, String outputPath, String outputType, ExportType exportType) {
		File inputDir = new File(inputPath);
		if(!inputDir.exists()) {
			inputDir.mkdirs();
		}
		File outputDir = new File(outputPath);
		if(!outputDir.exists()) {
			outputDir.mkdirs();
		}
		
		List<File> files = FileUtil.listFiles(inputPath);
		
		Map<String, DesignFile> filesMap = new HashMap<>();
		ExcelReader reader = new ExcelReader();
		boolean hasDuplicateId = false;
		for(File file : files) {
			List<DesignFile> fileMetas = reader.read(file);
			
			for(DesignFile fileMeta : fileMetas) {
				DesignFile langFileMeta = handleLang(fileMeta);
				filesMap.put(fileMeta.getName(), langFileMeta);
				
				hasDuplicateId = hasDuplicateId || langFileMeta.hasDuplicateId();
			}
		}
		if(hasDuplicateId) {
			throw new RuntimeException("发现重复ID，请往上翻查日志");
		}
		return filesMap.values();
	}
	
	public static DesignFile handleLang(DesignFile oriFileMeta) {
		List<String> names = new ArrayList<>();
		List<String> fieldTypes = new ArrayList<>();
		List<String> readBys = new ArrayList<>();
		List<String> comments = new ArrayList<>();
		List<List<String>> datas = new ArrayList<>();
		
		for(int i = 0; i < oriFileMeta.getFields().size(); i++) {
			DesignField field = oriFileMeta.getFields().get(i);
			
			String type = field.getFieldType();
			String head = field.getName();
			
			names.add(head);
			fieldTypes.add(type);
			readBys.add(field.getReadBy());
			comments.add(field.getComment());
			
			if("lang".equals(type)) {
				names.add(head + "Lang");
				fieldTypes.add("string");
				readBys.add("C");
				comments.add(field.getComment());
			}
			//这里需要注意，数据是一列一列地填充回去的
			for(int j = 0; j < oriFileMeta.getDatas().size(); j++) {
				String[] oriRowDatas = oriFileMeta.getDatas().get(j);
				
                if(datas.size() < j + 1) {
                    datas.add(new ArrayList<>());
                }
                List<String> newRowDatas = datas.get(j);
				
				if(i >= oriRowDatas.length) {
					newRowDatas.add("");
				} else {
					newRowDatas.add(oriRowDatas[i]);
				}
				if("lang".equals(type)) {
					String upperHead = head.substring(0, 1).toUpperCase() + head.substring(1);
					newRowDatas.add(oriFileMeta.getClassName() + "_" + upperHead + "_" + oriRowDatas[0]);
				}
			}
		}
		
		DesignFile newFileMeta = new DesignFile();
		newFileMeta.setName(oriFileMeta.getName());
		newFileMeta.fillMacro(oriFileMeta.getMacros());
		newFileMeta.fillField(names.toArray(new String[names.size()]), fieldTypes.toArray(new String[names.size()]),
				readBys.toArray(new String[names.size()]), comments.toArray(new String[names.size()]));
		List<String[]> resultDatas = new ArrayList<>();
		for(List<String> rowData : datas) {
			resultDatas.add(rowData.toArray(new String[rowData.size()]));
		}
		newFileMeta.fillData(resultDatas);
		return newFileMeta;
	}
	
	public static void export(Collection<DesignFile> fileMetas, String outputPath, String outputType, ExportType exportType) {
		if("csv".equals(outputType)) {
			CsvExporter.export(fileMetas, outputPath + "-" + outputType, exportType);
		} else if("json".equals(outputType)) {
			JsonExporter.export(fileMetas, outputPath + "-" + outputType, exportType);
		} else {
			System.out.println("无法处理的导出类型" + outputType);
		}
	}
	
	public static boolean isExport(ExportType readType, ExportType exportType) {
		if(readType == ExportType.NEITHER) {
			return false;
		}
		boolean isExport = ExportType.BOTH == readType || ExportType.BOTH == exportType || readType == exportType;
		return isExport;
	}
}
