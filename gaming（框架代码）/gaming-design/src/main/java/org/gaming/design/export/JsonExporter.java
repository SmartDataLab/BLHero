/**
 * 
 */
package org.gaming.design.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.gaming.design.loader.DesignField;
import org.gaming.design.loader.DesignFile;
import org.gaming.design.meta.ExportType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author YY
 *
 */
public class JsonExporter {

	public static void export(Collection<DesignFile> fileMetas, String outputPath, ExportType exportType) {
		for (DesignFile fileMeta : fileMetas) {
			if(!fileMeta.isExport(exportType)) {
				System.out.println("导出忽略----->" + fileMeta.getName());
				continue;
			}
			try {
				JsonObject result = new JsonObject();

				JsonArray marcos = new JsonArray();
				for (String marco : fileMeta.getMacros()) {
					marcos.add(marco);
				}
				result.add("marcos", marcos);

				JsonArray fields = new JsonArray();
				for (DesignField field : fileMeta.getFields()) {
					boolean isExport = DesignExporter.isExport(ExportType.vof(field.getReadBy()), exportType);
					if (!isExport) {
						continue;
					}
					
					JsonObject obj = new JsonObject();
					obj.addProperty("name", field.getName());
					obj.addProperty("fieldType", field.getFieldType());
					obj.addProperty("readBy", field.getReadBy());
					obj.addProperty("comment", field.getComment());
					fields.add(obj);
				}
				result.add("fields", fields);

				JsonArray datas = new JsonArray();
				for (String[] data : fileMeta.getDatas()) {
					JsonArray obj = new JsonArray();
					
					for (int i = 0; i < fileMeta.getFields().size(); i++) {
						DesignField field = fileMeta.getFields().get(i);
						boolean isExport = DesignExporter.isExport(ExportType.vof(field.getReadBy()), exportType);
						if (!isExport) {
							continue;
						}
						if (i >= data.length) {
							obj.add("");
						} else {
							obj.add(data[i]);
						}
					}
					datas.add(obj);
				}
				result.add("datas", datas);

				File file = new File(outputPath + File.separator + fileMeta.getName() + ".json");
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
				osw.write(result.toString());
				osw.close();
				System.out.println("导出----->" + fileMeta.getName());
			} catch (Exception e) {
				System.out.println("导出异常----->" + fileMeta.getName());
				e.printStackTrace();
			}
		}
	}
}
