package org.gaming.design.loader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.design.meta.ExportType;

/**
 * 读取的文件元数据
 */
public class DesignFile {
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 对应的类名
	 */
	private String className;
	/**
	 * ID字段名称
	 */
	private String idFieldName;
	/**
	 * 文件上的宏
	 */
	private String[] macros;
	/**
	 * 字段定义
	 */
	private List<DesignField> fields = new ArrayList<>();
	/**
	 * 文件数据
	 */
	private List<String[]> datas;
	/**
	 * 导出类型
	 */
	private ExportType exportType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getMacros() {
		return macros;
	}

	public void fillMacro(String[] macros) {
		this.macros = macros;
		String className = null;
		String idFieldName = null;
		ExportType exportType = ExportType.BOTH;
		for (String mac : macros) {
			if (mac.startsWith("#name")) {
				String[] parts = mac.split(" ");
				className = parts[1];
			} else if (mac.startsWith("#unique")) {
				String[] parts = mac.split(" ");
				idFieldName = parts[1];
			} else if (mac.startsWith("#export")) {
				String[] parts = mac.split(" ");
				exportType = ExportType.vof(parts[1]);
			}
		}
		if (className == null || idFieldName == null) {
			throw new RuntimeException("未定义类名与唯一ID");
		}
		this.className = className;
		this.idFieldName = idFieldName;
		this.exportType = exportType;
	}

	public List<String[]> getDatas() {
		return datas;
	}

	public void fillField(String[] names, String[] types, String[] readBys, String[] comments) {
		if (names.length != types.length || names.length != readBys.length || names.length != comments.length) {
			throw new RuntimeException("字段描述信息有欠缺，字段名、类型、读取方式、备注需要长度一致");
		}
		for (int i = 0; i < names.length; i++) {
			DesignField field = new DesignField();
			field.setName(names[i]);
			field.setFieldType(types[i]);
			field.setReadBy(readBys[i]);
			field.setComment(comments[i]);
			this.fields.add(field);
		}
	}

	public void fillData(List<String[]> rowDatas) {
		List<String[]> resultRowDatas = new ArrayList<>();
		for (String[] oriRowDatas : rowDatas) {
			boolean allEmpty = true;
			for (String value : oriRowDatas) {
				if (!"".equals(value)) {
					allEmpty = false;
					break;
				}
			}
			if (!allEmpty) {
				resultRowDatas.add(oriRowDatas);
			}
		}
		this.datas = resultRowDatas;
	}

	public String getClassName() {
		return className;
	}

	public List<DesignField> getFields() {
		return fields;
	}

	/**
	 * 检查重复ID
	 */
	public boolean hasDuplicateId() {
		int idIndex = 0;
		for (int i = 0; i < this.fields.size(); i++) {
			DesignField field = this.fields.get(i);
			if (field.getName().equals(idFieldName)) {
				idIndex = i;
				break;
			}
		}

		boolean hasDuplicateId = false;
		Set<String> checkIdDuplicate = new HashSet<>();
		for (String[] values : this.datas) {
			String idValue = values[idIndex];
			if (checkIdDuplicate.contains(idValue)) {
				System.out.println(String.format("%s发现重复ID%s", this.name, idValue));
				hasDuplicateId = true;
			}
			checkIdDuplicate.add(idValue);
		}
		return hasDuplicateId;
	}

	public ExportType getExportType() {
		return exportType;
	}

	public boolean isExport(ExportType exportType) {
		return this.exportType == ExportType.BOTH || this.exportType == exportType;
	}
}
