/**
 * 
 */
package com.xiugou.x1.excel.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gaming.design.export.DesignExporter;
import org.gaming.design.loader.DesignField;
import org.gaming.design.loader.DesignFile;
import org.gaming.design.meta.ExportType;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author YY
 *
 */
public class JsonExporter {

	static Gson gson = new Gson();

	public static void export(Collection<DesignFile> fileMetas, String outputPath, ExportType exportType) {
		for (DesignFile fileMeta : fileMetas) {
			if(!fileMeta.isExport(exportType)) {
				System.out.println("导出忽略----->" + fileMeta.getName());
				continue;
			}
			try {
				List<JsonObject> list = new ArrayList<>();
				for (String[] data : fileMeta.getDatas()) {
					JsonObject jsonObj = new JsonObject();
					for (int i = 0; i < fileMeta.getFields().size(); i++) {
						DesignField field = fileMeta.getFields().get(i);
						if ("-".equals(field.getReadBy())) {
							continue;
						}
						boolean isExport = DesignExporter.isExport(ExportType.vof(field.getReadBy()), exportType);
						if (!isExport) {
							continue;
						}
						String dataValue = null;
						if (i >= data.length) {
							dataValue = "";
						} else {
							dataValue = data[i];
						}
						if ("int".equals(field.getFieldType())) {
							Number num = gson.fromJson(dataValue, Integer.class);
							jsonObj.addProperty(field.getName(), num);
						} else if ("long".equals(field.getFieldType())) {
							Number num = gson.fromJson(dataValue, Long.class);
							jsonObj.addProperty(field.getName(), num);
						} else if ("string".equals(field.getFieldType())) {
							jsonObj.addProperty(field.getName(), dataValue);
						} else {
							try {
								Number num = gson.fromJson(dataValue, BigDecimal.class);
								jsonObj.addProperty(field.getName(), num);
							} catch (Exception e1) {
								try {
									JsonArray array = gson.fromJson(dataValue, JsonArray.class);
									jsonObj.add(field.getName(), array);
								} catch (Exception e2) {
									try {
										JsonObject object = gson.fromJson(dataValue, JsonObject.class);
										jsonObj.add(field.getName(), object);
									} catch (Exception e3) {
										jsonObj.addProperty(field.getName(), dataValue);
									}
								}
							}
						}
					}
					list.add(jsonObj);
				}

				File file = new File(outputPath + File.separator + fileMeta.getName() + ".json");
				try {
					OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
					osw.write(gson.toJson(list));
					osw.close();
					System.out.println("导出----->" + file.getName());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				System.out.println(fileMeta.getName() + "导出异常");
			}
		}
	}
}
