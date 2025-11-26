/**
 * 
 */
package org.gaming.design.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.design.loader.DesignField;
import org.gaming.design.loader.DesignFile;
import org.gaming.tool.FileUtil;

/**
 * @author YY
 *
 */
public class LanguageFileGenerator {

	/**
	 * 生成语言文件
	 * @param excelDir
	 * @param langFileName
	 * @param langClassName
	 */
	public static void genLangFile(String excelDir, String langFileName, String langClassName) {
		List<File> files = FileUtil.listFiles(excelDir);
		
		ExcelReader reader = new ExcelReader();
		
		//所有的语言数据
		List<String[]> cnLangs = new ArrayList<>();
		
		DesignFile langFileMeta = null;
		
		for(File file : files) {
			List<DesignFile> fileMetas = reader.read(file);
			
			for(DesignFile fileMeta : fileMetas) {
				try {
					if(langClassName.equals(fileMeta.getClassName())) {
						langFileMeta = fileMeta;
						continue;
					}
					for(int i = 0; i < fileMeta.getFields().size(); i++) {
						DesignField field = fileMeta.getFields().get(i);
						
						String type = field.getFieldType();
						String head = field.getName();
						String upperHead = head.substring(0, 1).toUpperCase() + head.substring(1);
						if(!"lang".equals(type)) {
							continue;
						}
						for(String[] rowDatas : fileMeta.getDatas()) {
							String[] langRows = new String[2];
							langRows[0] = fileMeta.getClassName() + "_" + upperHead + "_" + rowDatas[0];
							if(i >= rowDatas.length) {
								langRows[1] = "";
							} else {
								langRows[1] = rowDatas[i];
							}
							cnLangs.add(langRows);
						}
					}
				} catch (Exception e) {
					System.out.println(fileMeta.getName() + "异常" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		//导出到excel
		if(langFileMeta == null) {
			langFileMeta = new DesignFile();
			List<String> macros = new ArrayList<>();
			macros.add("#name " + langClassName);
			macros.add("#unique id");
			macros.add("#index langId");
			
			List<String> names = new ArrayList<>();
			List<String> types = new ArrayList<>();
			List<String> readBys = new ArrayList<>();
			List<String> comments = new ArrayList<>();
			
			names.add("id");
			types.add("int");
			readBys.add("*");
			comments.add("序号");
			
			names.add("langId");
			types.add("string");
			readBys.add("*");
			comments.add("语言ID");
			
			names.add("cn");
			types.add("string");
			readBys.add("C");
			comments.add("简体中文");
			
			List<String[]> rowDatas = new ArrayList<>();
			for (int i = 0; i < cnLangs.size(); i++) {
				String[] rowData = new String[3];
				rowData[0] = (i + 1) + "";
				rowData[1] = cnLangs.get(i)[0];
				rowData[2] = cnLangs.get(i)[1];
				rowDatas.add(rowData);
			}
			
			langFileMeta.fillMacro(macros.toArray(new String[macros.size()]));
			langFileMeta.fillField(names.toArray(new String[names.size()]), types.toArray(new String[names.size()]),
					readBys.toArray(new String[names.size()]), comments.toArray(new String[names.size()]));
			langFileMeta.fillData(rowDatas);
			ExcelWriter.write(excelDir, langFileName, langFileMeta);
		} else {
			Map<String, String[]> dataMap = new LinkedHashMap<>();
			for(String[] rowData : langFileMeta.getDatas()) {
				dataMap.put(rowData[1], rowData);
			}
			
			boolean needToUpdate = false;
			Set<String> repeatedKeys = new HashSet<>();
			for (int i = 0; i < cnLangs.size(); i++) {
				String[] cnLang = cnLangs.get(i);
				if(repeatedKeys.contains(cnLang[0])) {
					System.out.println("发现重复的语言主键" + cnLang[0]);
				}
				repeatedKeys.add(cnLang[0]);
				String[] oldCnLang = dataMap.get(cnLang[0]);
				if(oldCnLang == null) {
					needToUpdate = true;
					break;
				}
				if(!cnLang[1].equals(oldCnLang[2])) {
					System.out.println(Arrays.toString(oldCnLang));
					needToUpdate = true;
					break;
				}
			}
			if(!needToUpdate) {
				System.out.println("语言表没有变化，不重新导出");
				return;
			}
			
			List<String[]> resultLangs = new ArrayList<>();
			for (int i = 0; i < cnLangs.size(); i++) {
				String[] langContent = cnLangs.get(i);
				String langKey = langContent[0];
				String[] oriData = dataMap.get(langKey);
				if(oriData != null) {
					//已被填充过的数据，保留填充过的内容，比如已经填充过en、tw、jp等其他数据
					String[] rowData = new String[oriData.length];
					System.arraycopy(oriData, 0, rowData, 0, oriData.length);
					rowData[0] = (i + 1) + "";
					
					//如果语言表中的语言内容跟将要生成的内容不一样，即语言描述已经发生改变，那么应该使用新的数据
					if(!rowData[2].equals(langContent[1])) {
						rowData[2] = langContent[1];
						for(int j = 3; j < rowData.length; j++) {
							rowData[j] = "";
						}
					}
					resultLangs.add(rowData);
				} else {
					String[] rowData = new String[3];
					rowData[0] = (i + 1) + "";
					rowData[1] = langContent[0];
					rowData[2] = langContent[1];
					resultLangs.add(rowData);
				}
			}
			langFileMeta.fillData(resultLangs);
			ExcelWriter.write(excelDir, langFileName, langFileMeta);
		}
	}
}
