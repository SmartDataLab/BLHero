/**
 * 
 */
package org.gaming.design.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.gaming.design.loader.DesignFile;
import org.gaming.design.loader.IDesignReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author YY
 *
 */
public class CsvReader implements IDesignReader {

	private static Logger logger = LoggerFactory.getLogger(CsvReader.class);
	
	public List<DesignFile> read(File file) {
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            List<String[]> rowDatas = csvReader.readAll();
            
            int firstRowNum = 0;
            for(int i = 0; i < rowDatas.size(); i++) {
            	String[] rowData = rowDatas.get(i);
            	if(rowData[0].startsWith("#name")) {
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
            
            String fileName = file.getName().substring(0, file.getName().indexOf("."));
            fileMeta.setName(fileName);
            return Collections.singletonList(fileMeta);
        } catch (Exception e) {
        	logger.error(String.format("%s loading config error! ", file.getName()), e);
            return Collections.emptyList();
        }
	}
}
