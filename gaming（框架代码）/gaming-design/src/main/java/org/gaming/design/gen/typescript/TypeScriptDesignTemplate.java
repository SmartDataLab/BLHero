/**
 * 
 */
package org.gaming.design.gen.typescript;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.design.gen.GeneratePart;
import org.gaming.design.gen.GenerateTemplate;
import org.gaming.design.loader.DesignFile;
import org.gaming.design.loader.IDesignReader;
import org.gaming.design.meta.DesignClassMeta;
import org.gaming.design.meta.DesignCollectorMeta;
import org.gaming.design.meta.DesignColumnMeta;
import org.gaming.design.meta.DesignConstData;
import org.gaming.design.meta.DesignIndexMeta;
import org.gaming.design.meta.ExportType;
import org.gaming.tool.CodeBuilder;
import org.gaming.tool.FileUtil;

/**
 * @author YY
 *
 */
public class TypeScriptDesignTemplate extends GenerateTemplate {

	private TypeScriptGen gen;
	private Map<String, String> formatters = new HashMap<>();
	
	public void addFormator(String designType, String classType) {
		formatters.put(designType, classType);
		formatters.put(designType + "[]", listDeclare(classType));
	}
	
	public TypeScriptDesignTemplate(String genFilePath) {
		this.gen = TypeScriptGen.readGenFile(genFilePath);
		this.addFormator("lang", "string");
		this.addFormator("int", "number");
		this.addFormator("long", "number");
		this.addFormator("string", "string");
		this.addFormator("float", "number");
		
		for(String format : gen.getFormats()) {
			String[] parts = format.split(" ");
			this.addFormator(parts[0], parts[1]);
		}
		
		parts.add(new GeneratePart() {
			@Override
			public String methodPrefix() {
				return "getIn";
			}
			@Override
			public String nullHandle(String cacheName, String indexName, String fieldNames) {
				return String.format("throw new DesignDataNotFoundError(\"%s.getIn%s\", %s);", cacheName, indexName, fieldNames);
			}
		});
		parts.add(new GeneratePart() {
			@Override
			public String methodPrefix() {
				return "findIn";
			}
			@Override
			public String nullHandle(String cacheName, String indexName, String fieldNames) {
				return "return null;";
			}
		});
	}
	
	@Override
	public void gen(String outputPath, String exportSourcePath, IDesignReader designReader, ExportType exportType) {
		File designFolder = new File(exportSourcePath);
		for (File file : designFolder.listFiles()) {
			try {
				List<DesignFile> fileMetas = designReader.read(file);
				for (DesignFile fileMeta : fileMetas) {
					DesignClassMeta classMeta = DesignClassMeta.build(fileMeta, "", "");
					if(!this.isExport(classMeta, exportType)) {
						continue;
					}
					String classContent = this.genCode(classMeta);

					FileUtil.writeToFile(outputPath, classMeta.getCacheName() + ".ts", classContent);
					System.out.println("生成" + classMeta.getAbstractName());
				}
			} catch (Exception e) {
				System.out.println(file.getName() + "生成异常");
				e.printStackTrace();
			}
		}
		System.out.println("生成结束");
	}
	
	private String genIndexDefine(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String structDefine = formatIndexStruct(classMeta, indexMeta, 0);
			builder.NTFormat("protected %s: %s = null;", indexMeta.getName(), structDefine);
		}
		return builder.toString();
	}
	
	private String genCollectorDefine(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String structDefine = formatCollectorStruct(classMeta, collectorMeta, 0);
			builder.NTFormat("protected %s: %s = null;", collectorMeta.getName(), structDefine);
		}
		return builder.toString();
	}
	
	protected String genIndexMethod(DesignClassMeta classMeta, GeneratePart part) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String upperIndexName = indexMeta.getName().substring(0, 1).toUpperCase() + indexMeta.getName().substring(1);
			builder.NTFormat("public %s%s(", part.methodPrefix(), upperIndexName); 
			
			//写函数签名、参数列表
			boolean firstParam = true;
			String fieldNames = "";
			for(String field : indexMeta.getFields()) {
				if(!firstParam) {
					builder.append(", ");
					fieldNames += ", ";
				}
				DesignColumnMeta columnMeta = classMeta.nameToColumns().get(field);
				String typeExplain = formatters.getOrDefault(columnMeta.getType(), columnMeta.getType());
				builder.format("%s: %s", columnMeta.getName(), typeExplain);
				fieldNames += columnMeta.getName();
				firstParam = false;
			}
			builder.format("): %s {", formatClassName(classMeta));
			
			String currMap = "this." + indexMeta.getName();
			for(int i = 0; i < indexMeta.getFields().size(); i++) {
				String field = indexMeta.getFields().get(i);
				if(i < indexMeta.getFields().size() - 1) {
					String subStruct = formatIndexSubStruct(classMeta, indexMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					builder.NTTFormat("let %s: %s = %s.get(%s);", layerMap, subStruct, currMap, field);
					builder.NTTFormat("if(%s === undefined) {", layerMap);
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					currMap = layerMap;
				} else {
					builder.NTTFormat("let t: %s = %s.get(%s);", formatClassName(classMeta), currMap, field);
					builder.NTTFormat("if(t === undefined) {");
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					builder.NTTFormat("return t;");
				}
			}
			builder.NTFormat("}");
		}
		return builder.toString();
	}
	
	protected String genCollectorMethod(DesignClassMeta classMeta, GeneratePart part) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String upperIndexName = collectorMeta.getName().substring(0, 1).toUpperCase() + collectorMeta.getName().substring(1);
			builder.NTFormat("public %s%s(", part.methodPrefix(), upperIndexName);
			
			//写函数签名、参数列表
			boolean firstParam = true;
			String fieldNames = "";
			for(String field : collectorMeta.getFields()) {
				if(!firstParam) {
					builder.append(", ");
					fieldNames += ", ";
				}
				DesignColumnMeta columnMeta = classMeta.nameToColumns().get(field);
				String typeExplain = formatters.getOrDefault(columnMeta.getType(), columnMeta.getType());
				builder.format("%s: %s", columnMeta.getName(), typeExplain);
				fieldNames += columnMeta.getName();
				firstParam = false;
			}
			builder.format(") : %s[] {", formatClassName(classMeta));
			
			String currMap = "this." + collectorMeta.getName();
			for(int i = 0; i < collectorMeta.getFields().size(); i++) {
				String field = collectorMeta.getFields().get(i);
				if(i < collectorMeta.getFields().size() - 1) {
					String subStruct = formatCollectorSubStruct(classMeta, collectorMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTFormat("let %s: %s = %s.get(%s);", layerMap, subStruct, currMap, field);
					builder.NTTFormat("if(%s === undefined) {", layerMap);
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					currMap = layerMap;
				} else {
					builder.NTTFormat("let ts: %s[] = %s.get(%s);", formatClassName(classMeta), currMap, field);
					builder.NTTFormat("if(ts === undefined) {");
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					builder.NTTFormat("return ts;", currMap, field);
				}
			}
			builder.NTFormat("}");
		}
		return builder.toString();
	}
	
	
	private String genLoadAutoGenerate(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("protected loadAutoGenerate(): void {");
		
		builder.append(genIndexFill(classMeta));
		builder.append(genCollectorFill(classMeta));
		//TODO
		builder.append(genConstFill(classMeta));
		
		builder.NTFormat("}");
		return builder.toString();
	}
	
	protected String genIndexFill(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String structDefine = formatIndexStruct(classMeta, indexMeta, 0);
			builder.NTTFormat("//构建索引%s", indexMeta.getName());
			builder.NTTFormat("let %s: %s = new %s();", indexMeta.getName(), structDefine, structDefine);
			builder.NTTFormat("for(let i = 0; i < this.all().length; i++) {");
			builder.NTTTFormat("let data: %s = this.all()[i];", formatClassName(classMeta));
			
			String currLayerMap = indexMeta.getName();
			
			for(int i = 0; i < indexMeta.getFields().size(); i++) {
				String fieldName = indexMeta.getFields().get(i);
				String upperFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				
				if(i < indexMeta.getFields().size() - 1) {
					String subStruct = formatIndexSubStruct(classMeta, indexMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTTFormat("let %s: %s = %s.get(data.get%s());", layerMap, subStruct, currLayerMap, upperFieldName);
					builder.NTTTFormat("if(%s === undefined) {", layerMap);
					builder.NTTTTFormat("%s = new %s();", layerMap, subStruct);
					builder.NTTTTFormat("%s.set(data.get%s(), %s);", currLayerMap, upperFieldName, layerMap);
					builder.NTTTFormat("}");
					currLayerMap = layerMap;
				} else {
					//最后一层是存数据的
					builder.NTTTFormat("%s.set(data.get%s(), data);", currLayerMap, upperFieldName);
				}
			}
			builder.NTTFormat("}");
			builder.NTTFormat("this.%s = %s;", indexMeta.getName(), indexMeta.getName());
		}
		return builder.toString();
	}
	
	protected String genCollectorFill(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String structDefine = formatCollectorStruct(classMeta, collectorMeta, 0);
			builder.NTTFormat("//构建收集器%s", collectorMeta.getName());
			builder.NTTFormat("let %s: %s = new %s();", collectorMeta.getName(), structDefine, structDefine);
			builder.NTTFormat("for(let i = 0; i < this.all().length; i++) {");
			builder.NTTTFormat("let data: %s = this.all()[i];", formatClassName(classMeta));
			
			String currLayerMap = collectorMeta.getName();
			
			for(int i = 0; i < collectorMeta.getFields().size(); i++) {
				String fieldName = collectorMeta.getFields().get(i);
				String upperFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				
				if(i < collectorMeta.getFields().size() - 1) {
					String subStruct = formatCollectorSubStruct(classMeta, collectorMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTTFormat("let %s: %s = %s.get(data.get%s());", layerMap, subStruct, currLayerMap, upperFieldName);
					builder.NTTTFormat("if(%s === undefined) {", layerMap);
					builder.NTTTTFormat("%s = new %s();", layerMap, subStruct);
					builder.NTTTTFormat("%s.set(data.get%s(), %s);", currLayerMap, upperFieldName, layerMap);
					builder.NTTTFormat("}");
					currLayerMap = layerMap;
				} else {
					builder.NTTTFormat("let collector: %s[] = %s.get(data.get%s());", formatClassName(classMeta), currLayerMap, upperFieldName);
					builder.NTTTFormat("if(collector === undefined) {");
					builder.NTTTTFormat("collector = [];", formatClassName(classMeta));
					builder.NTTTTFormat("%s.set(data.get%s(), collector);", currLayerMap, upperFieldName);
					builder.NTTTFormat("}");
					builder.NTTTFormat("collector.push(data);");
				}
			}
			builder.NTTFormat("}");
			builder.NTTFormat("this.%s = %s;", collectorMeta.getName(), collectorMeta.getName());
		}
		return builder.toString();
	}
	
	private String genCustom(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(String custom : gen.getCustoms()) {
			builder.NFormat(custom.replaceAll("%s", classMeta.getCacheName()));
		}
		return builder.toString();
	}
//		CodeBuilder builder = new CodeBuilder();
//		builder.NTFormat("private static instance: %s = null;", classMeta.getCacheName());
//		builder.NTFormat("public static get Instance(): %s {", classMeta.getCacheName());
//		builder.NTTFormat("if(%s.instance === null) {", classMeta.getCacheName());
//		builder.NTTTFormat("%s.instance = new %s();", classMeta.getCacheName(), classMeta.getCacheName());
//		builder.NTTTFormat("let dataList: object[] = App.ResManager.readConfigFile(\"gamecfg/\" + %s.instance.fileName);", classMeta.getCacheName());
//		builder.NTTTFormat("%s.instance.LoadObjects(dataList, new X1SeparatorDesignParser());", classMeta.getCacheName());
//		builder.NTTFormat("}");
//		builder.NTTFormat("return %s.instance;", classMeta.getCacheName());
//		builder.NTFormat("}");
//		return builder.toString();
	
	private String genCreateInstance(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("protected createInstance(): %s {", formatClassName(classMeta));
		builder.NTTFormat("return new %s();", formatClassName(classMeta));
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private String genFileName(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public get fileName(): string {");
		builder.NTTFormat("return \"%s\";", classMeta.getFileName());
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private String genEntityClass(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NFormat("//当前类代码由导表工具生成，请勿修改");
		builder.NFormat("export class %s implements IDesignData {", formatClassName(classMeta));
		for(DesignColumnMeta columnMeta : classMeta.getColumnMetas()) {
			builder.NTFormat("//%s", columnMeta.getComment());
			String typeExplain = formatters.getOrDefault(columnMeta.getType(), columnMeta.getType());
			if("number".equals(typeExplain)) {
				builder.NTFormat("protected %s: %s = 0;", columnMeta.getName(), typeExplain);
			} else if("string".equals(typeExplain)) {
				builder.NTFormat("protected %s: %s = \"\";", columnMeta.getName(), typeExplain);
			} else if(typeExplain.endsWith("[]")) {
				builder.NTFormat("protected %s: %s = [];", columnMeta.getName(), typeExplain);
			} else {
				builder.NTFormat("protected %s: %s = null;", columnMeta.getName(), typeExplain);
			}
		}
		//写getId函数
		builder.NTFormat("public Id(): number {");
		builder.NTTFormat("return this.%s;", classMeta.getIdDefine());
		builder.NTFormat("}");
		//写getter
		for(DesignColumnMeta columnMeta : classMeta.getColumnMetas()) {
			String upperFieldName = columnMeta.getName().substring(0, 1).toUpperCase() + columnMeta.getName().substring(1);
			String typeExplain = formatters.getOrDefault(columnMeta.getType(), columnMeta.getType());
			
			builder.NTFormat("public get%s(): %s {", upperFieldName, typeExplain);
			builder.NTTFormat("return this.%s;", columnMeta.getName());
			builder.NTFormat("}");
		}
		//写format
		for(DesignColumnMeta columnMeta : classMeta.getColumnMetas()) {
			String upperFieldName = columnMeta.getName().substring(0, 1).toUpperCase() + columnMeta.getName().substring(1);
			String typeExplain = formatters.getOrDefault(columnMeta.getType(), columnMeta.getType());
			if("number".equals(typeExplain)) {
				continue;
			} else if("string".equals(typeExplain)) {
				continue;
			} else if(typeExplain.endsWith("[]")) {
				builder.NTFormat("private format%s(): %s {", upperFieldName, typeExplain.replace("[]", ""));
				if(typeExplain.startsWith("number")) {
					builder.NTTFormat("return 0;");
				} else if(typeExplain.startsWith("string")) {
					builder.NTTFormat("return \"\";");
				} else {
					builder.NTTFormat("return new %s();", typeExplain.replace("[]", ""));
				}
				builder.NTFormat("}");
			} else {
				builder.NTFormat("private format%s(): %s {", upperFieldName, typeExplain);
				builder.NTTFormat("return new %s();", typeExplain);
				builder.NTFormat("}");
			}
		}
		builder.NFormat("}");
		return builder.toString();
	}
	
	public String genCode(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(String import0 : gen.getImports()) {
			builder.NFormat("%s", import0);
		}
		builder.NFormat("//当前类代码由导表工具生成，请勿修改");
		builder.NFormat("export default class %s extends DesignCache<%s> {", classMeta.getCacheName(), formatClassName(classMeta));
		builder.append(genCustom(classMeta));
		builder.append(genFileName(classMeta));
		builder.append(genCreateInstance(classMeta));
		
		builder.NFormat("");
		builder.append(genIndexDefine(classMeta));
		builder.NFormat("");
		builder.append(genCollectorDefine(classMeta));
		builder.NFormat("");
		builder.append(genLoadAutoGenerate(classMeta));
		builder.NFormat("");
		for(String result : genIndexMethods(classMeta)) {
			builder.append(result);
			builder.NFormat("");
		}
		for(String result : genCollectorMethods(classMeta)) {
			builder.append(result);
			builder.NFormat("");
		}
		builder.append(genConstFields(classMeta));
		builder.NFormat("}");
		builder.append(genEntityClass(classMeta));
		return builder.toString();
	}
	
	@Override
	protected String mapDeclare() {
		return "Map<#key#, #value#>";
	}

	@Override
	protected String listDeclare(String elementType) {
		return elementType + "[]";
	}

	@Override
	protected String typeExplain(String typeName) {
		return formatters.getOrDefault(typeName, typeName);
	}

	@Override
	public String formatClassName(DesignClassMeta classMeta) {
		return String.format(gen.getClassname(), classMeta.getRawClassName());
	}
	
	private String genConstFields(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignConstData constData : classMeta.getConstDatas()) {
			String typeExplain = typeExplain(constData.getType());
			String upperKeyName = DesignClassMeta.upperFirst(constData.getKey());
			if("number".equals(typeExplain)) {
				builder.NTFormat("protected %s: %s = 0;", constData.getKey(), typeExplain);
			} else if("string".equals(typeExplain)) {
				builder.NTFormat("protected %s: %s = \"\";", constData.getKey(), typeExplain);
			} else if(typeExplain.endsWith("[]")) {
				builder.NTFormat("protected %s: %s = [];", constData.getKey(), typeExplain);
			} else {
				builder.NTFormat("protected %s: %s = null;", constData.getKey(), typeExplain);
			}
			builder.NTFormat("public get%s(): %s {", upperKeyName, typeExplain);
			builder.NTTFormat("return this.%s;", constData.getKey());
			builder.NTFormat("}");
			
			//写format
			if("number".equals(typeExplain)) {
			} else if("string".equals(typeExplain)) {
			} else if(typeExplain.endsWith("[]")) {
				builder.NTFormat("private format%s(): %s {", upperKeyName, typeExplain.replace("[]", ""));
				if(typeExplain.startsWith("number")) {
					builder.NTTFormat("return 0;");
				} else if(typeExplain.startsWith("string")) {
					builder.NTTFormat("return \"\";");
				} else {
					builder.NTTFormat("return new %s();", typeExplain.replace("[]", ""));
				}
				builder.NTFormat("}");
			} else {
				builder.NTFormat("private format%s(): %s {", upperKeyName, typeExplain);
				builder.NTTFormat("return new %s();", typeExplain);
				builder.NTFormat("}");
			}
		}
		return builder.toString();
	}
	
	private String genConstFill(DesignClassMeta classMeta) {
		//TODO 
		CodeBuilder builder = new CodeBuilder();
		if(classMeta.getConstDatas().isEmpty()) {
			return builder.toString();
		}
		String upperKeyName = DesignClassMeta.upperFirst(classMeta.getConstKeyName());
		String upperValueName = DesignClassMeta.upperFirst(classMeta.getConstValueName());
		builder.NTTFormat("//构建常量");
		builder.NTTFormat("const constMap: Map<string, string> = new Map<string, string>();");
		builder.NTTFormat("for(let i = 0; i < this.all().length; i++) {");
		builder.NTTTFormat("let data: %s = this.all()[i];", formatClassName(classMeta));
		builder.NTTTFormat("constMap.set(data.get%s(), data.get%s());", upperKeyName, upperValueName);
		builder.NTTFormat("}");
		builder.NTTFormat("this.loadConst(constMap);");
		return builder.toString();
	}
}
