package org.gaming.design.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gaming.design.loader.DesignFile;
import org.gaming.design.loader.IDesignReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * excel处理方法
 */
public class ExcelReader implements IDesignReader {

	private static Logger logger = LoggerFactory.getLogger(ExcelReader.class);

	public List<DesignFile> read(File file) {
		Map<String, List<String[]>> sheetDataList = readFile(file);
		
		List<DesignFile> metaList = new ArrayList<>();

		for (Entry<String, List<String[]>> sheetData : sheetDataList.entrySet()) {
			try {
				List<String[]> rowDatas = sheetData.getValue();
				int firstRowNum = 0;
				for (int i = 0; i < rowDatas.size(); i++) {
					String[] rowData = rowDatas.get(i);
					if (rowData[0].startsWith("#name")) {
						firstRowNum = i;
						break;
					}
				}
				DesignFile fileMeta = new DesignFile();
				fileMeta.fillMacro(rowDatas.get(firstRowNum + 0));
				String[] names = rowDatas.get(firstRowNum + 1);
				String[] types = rowDatas.get(firstRowNum + 2);
				String[] readBys = rowDatas.get(firstRowNum + 3);
				String[] comments = rowDatas.get(firstRowNum + 4);
				fileMeta.fillField(names, types, readBys, comments);
				fileMeta.fillData(rowDatas.subList(firstRowNum + 5, rowDatas.size()));

				String fileName = file.getName().substring(0, file.getName().indexOf(".")) + "_"
						+ fileMeta.getClassName();
				fileMeta.setName(fileName);
				metaList.add(fileMeta);
			} catch (Exception e) {
				logger.error(String.format("%s.%s loading config error! ", file.getName(), sheetData.getKey()), e);
				System.out.println(String.format("%s.%s loading config error! ", file.getName(), sheetData.getKey()));
				e.printStackTrace();
			}
		}
		return metaList;
	}

	private Map<String, List<String[]>> readFile(File file) {
		Map<String, List<String[]>> data = new HashMap<>();
		if (file.getName().startsWith("~$")) {
			return data;
		}
		XSSFWorkbook workbook = null;
		try {
			// 读取多个Sheet表
			
			workbook = new XSSFWorkbook(new FileInputStream(file));
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				long time1 = System.currentTimeMillis();
				List<String[]> datas = readSheet(sheet, workbook);
				long time2 = System.currentTimeMillis();
				
				System.out.println("耗时----->" + file.getName() + "----->" + (time2 - time1));
				data.put(sheet.getSheetName(), datas);
			}
		} catch (Exception e) {
			System.out.println(String.format("{%s} loading error!", file.getName()));
			throw new RuntimeException(e);
		} finally {
			if(workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	private List<String[]> readSheet(XSSFSheet sheet, XSSFWorkbook workbook) {
		List<String[]> data = new ArrayList<>();
		
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			String[] strArr = new String[row.getLastCellNum() > 0 ? row.getLastCellNum() : 0];
			Arrays.fill(strArr, "");

			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				
				XSSFCell cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				switch (cell.getCellTypeEnum()) {
				case FORMULA:
					strArr[j] = cell.getRawValue();
					break;
				case NUMERIC:
					strArr[j] = cell.getRawValue();
					break;
				case STRING:
					strArr[j] = cell.getStringCellValue();
					break;
				case BOOLEAN:
					strArr[j] = String.valueOf(cell.getBooleanCellValue());
					break;
				default:
					break;
				}
			}
			data.add(strArr);
		}
		return data;
	}
}
