package org.gaming.design.export;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gaming.design.loader.DesignField;
import org.gaming.design.loader.DesignFile;

/**
 * excel处理方法
 */
public class ExcelWriter {

    public static void write(String filePath, String fileName, DesignFile fileMeta) {
    	XSSFWorkbook workbook = new XSSFWorkbook();
    	XSSFSheet sheet = workbook.createSheet("Sheet");
    	
    	writeTableHead(sheet, 0, fileMeta.getMacros());
    	
    	String[] names = new String[fileMeta.getFields().size()];
    	String[] fieldTypes = new String[fileMeta.getFields().size()];
    	String[] readBys = new String[fileMeta.getFields().size()];
    	String[] comments = new String[fileMeta.getFields().size()];
    	
    	for(int i = 0; i < fileMeta.getFields().size(); i++) {
    		DesignField field = fileMeta.getFields().get(i);
    		names[i] = field.getName();
    		fieldTypes[i] = field.getFieldType();
    		readBys[i] = field.getReadBy();
    		comments[i] = field.getComment();
    	}
    	writeTableHead(sheet, 1, names);
    	writeTableHead(sheet, 2, fieldTypes);
    	writeTableHead(sheet, 3, readBys);
    	writeTableHead(sheet, 4, comments);
    	
    	for(int i = 0; i < fileMeta.getFields().size(); i++) {
    		sheet.setColumnWidth(i, 256 * 20);
    	}
    	
    	int offset = 5;
    	for(int i = 0; i < fileMeta.getDatas().size(); i++) {
    		XSSFRow dataRow = sheet.createRow(i + offset);
    		for (int j = 0; j < fileMeta.getDatas().get(i).length; j++) {
    			String value = fileMeta.getDatas().get(i)[j];
    			
    			XSSFCell cell = dataRow.createCell(j);
        		cell.setCellValue(value);
			}
    	}
    	try {
			FileOutputStream fos = new FileOutputStream(filePath + File.separator + fileName);
			workbook.write(fos);
			fos.close();
			workbook.close();
			System.out.println("生成" + fileName + "完成");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static void writeTableHead(XSSFSheet sheet, int rowNum, String[] datas) {
    	XSSFRow row = sheet.createRow(rowNum);
    	for(int i = 0; i < datas.length; i++) {
    		XSSFCell cell = row.createCell(i);
    		cell.setCellValue(datas[i]);
    	}
    }
}
