/**
 * 
 */
package org.gaming.design.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.design.loader.DesignField;
import org.gaming.design.loader.DesignFile;

/**
 * @author YY
 *
 */
public class DesignClassMeta {
	private String className;
	// 前缀
	private String classNamePrefix;
	// 后缀
	private String classNamePostfix;
	private String fileName;
	private String idDefine;
	private List<DesignColumnMeta> columnMetas = new ArrayList<>();
	private List<DesignIndexMeta> indexMetas = new ArrayList<>();
	private List<DesignCollectorMeta> collectorMetas = new ArrayList<>();
	private List<DesignConstData> constDatas = new ArrayList<>();
	private String constKeyName;
	private String constValueName;

	private Map<String, DesignColumnMeta> nameToColumns;
	
	private ExportType exportType = ExportType.BOTH;
	
	/**
	 * 
	 * @param fileMeta
	 * @param postfix
	 *            后缀
	 * @return
	 */
	public static DesignClassMeta build(DesignFile fileMeta, String classNamePrefix, String classNamePostfix) {
		DesignClassMeta classMeta = new DesignClassMeta();
		classMeta.setFileName(fileMeta.getName());
		classMeta.setClassNamePrefix(classNamePrefix);
		classMeta.setClassNamePostfix(classNamePostfix);

		for (String str : fileMeta.getMacros()) {
			if (str.startsWith("#name")) {
				String[] strs = str.split(" ");
				String className = strs[1].trim();
				className = className.substring(0, 1).toUpperCase() + className.substring(1);
				classMeta.setClassName(className);

			} else if (str.startsWith("#collector")) {
				DesignCollectorMeta collectorMeta = new DesignCollectorMeta(str);
				classMeta.getCollectorMetas().add(collectorMeta);

			} else if (str.startsWith("#index")) {
				DesignIndexMeta indexMeta = new DesignIndexMeta(str);
				classMeta.getIndexMetas().add(indexMeta);

			} else if (str.startsWith("#unique")) {
				String[] strs = str.split(" ");
				classMeta.setIdDefine(strs[1].trim());

			} else if (str.startsWith("#const")) {
				String[] strs = str.split(" ");
				String constKey = strs[1].trim();
				String constType = strs[2].trim();
				String constValue = strs[3].trim();
				String constComment = "";
				if(strs.length > 4) {
					constComment = strs[4].trim();
				}

				int keyIndex = -1;
				int typeIndex = -1;
				int commentIndex = -1;

				for (int i = 0; i < fileMeta.getFields().size(); i++) {
					String head = fileMeta.getFields().get(i).getName();
					if (head.equals(constKey)) {
						keyIndex = i;
					}
					if (head.equals(constType)) {
						typeIndex = i;
					}
					if(head.equals(constComment)) {
						commentIndex = i;
					}
				}
				if (keyIndex < 0 || typeIndex < 0) {
					continue;
				}
				for (String[] datas : fileMeta.getDatas()) {
					if(commentIndex >= 0) {
						DesignConstData constData = new DesignConstData(datas[keyIndex], datas[typeIndex], datas[commentIndex]);
						classMeta.constDatas.add(constData);
					} else {
						DesignConstData constData = new DesignConstData(datas[keyIndex], datas[typeIndex], "");
						classMeta.constDatas.add(constData);
					}
				}
				classMeta.constKeyName = constKey;
				classMeta.constValueName = constValue;
			} else if(str.startsWith("#export")) {
				String[] strs = str.split(" ");
				String exportType = strs[1].trim();
				classMeta.exportType = ExportType.vof(exportType);
				
			} else if ("".equals(str)) {

			} else {
				// 什么玩意
				System.out.println(str + " " + fileMeta.getName());
			}
		}
		for (int i = 0; i < fileMeta.getFields().size(); i++) {
			DesignField field = fileMeta.getFields().get(i);
			String name = field.getName();
			String type = field.getFieldType();
			String readBy = field.getReadBy();
			String comment = field.getComment();
			classMeta.getColumnMetas().add(new DesignColumnMeta(name, type, comment, readBy));
		}
		return classMeta;
	}

	public Map<String, DesignColumnMeta> nameToColumns() {
		if (nameToColumns == null) {
			nameToColumns = new HashMap<>();
			for (DesignColumnMeta columnMeta : columnMetas) {
				nameToColumns.put(columnMeta.getName(), columnMeta);
			}
		}
		return nameToColumns;
	}

	public String getClassName() {
		return classNamePrefix + className + classNamePostfix;
	}
	
	public String getRawClassName() {
		return className;
	}

	public String getCacheName() {
		return className + "Cache";
	}

	public String getAbstractName() {
		return className + "AbstractCache";
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIdDefine() {
		return idDefine;
	}

	public void setIdDefine(String idDefine) {
		this.idDefine = idDefine;
	}

	public List<DesignColumnMeta> getColumnMetas() {
		return columnMetas;
	}

	public void setColumnMetas(List<DesignColumnMeta> columnMetas) {
		this.columnMetas = columnMetas;
	}

	public List<DesignIndexMeta> getIndexMetas() {
		return indexMetas;
	}

	public void setIndexMetas(List<DesignIndexMeta> indexMetas) {
		this.indexMetas = indexMetas;
	}

	public List<DesignCollectorMeta> getCollectorMetas() {
		return collectorMetas;
	}

	public void setCollectorMetas(List<DesignCollectorMeta> collectorMetas) {
		this.collectorMetas = collectorMetas;
	}

	public String getClassNamePostfix() {
		return classNamePostfix;
	}

	public void setClassNamePostfix(String classNamePostfix) {
		this.classNamePostfix = classNamePostfix;
	}

	public List<DesignConstData> getConstDatas() {
		return constDatas;
	}

	public String getConstKeyName() {
		return constKeyName;
	}

	public String getConstValueName() {
		return constValueName;
	}

	public void setConstKeyName(String constKeyName) {
		this.constKeyName = constKeyName;
	}

	public void setConstValueName(String constValueName) {
		this.constValueName = constValueName;
	}

	public String getClassNamePrefix() {
		return classNamePrefix;
	}

	public void setClassNamePrefix(String classNamePrefix) {
		this.classNamePrefix = classNamePrefix;
	}
	
	public static String upperFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public ExportType getExportType() {
		return exportType;
	}
}
