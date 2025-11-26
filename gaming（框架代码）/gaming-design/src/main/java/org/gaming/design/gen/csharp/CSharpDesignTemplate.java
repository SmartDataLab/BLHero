/**
 * 
 */
package org.gaming.design.gen.csharp;

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
public class CSharpDesignTemplate extends GenerateTemplate {

	private CSharpGen gen;
	private Map<String, String> formatters = new HashMap<>();
	
	public CSharpDesignTemplate(String genFilePath) {
		this.gen = CSharpGen.readGenFile(genFilePath);
		addFormator("int", "int");
		addFormator("long", "long");
		addFormator("string", "string");
		addFormator("float", "float");
		addFormator("lang", "string");
		
		for(String format : gen.getFormats()) {
			String[] parts = format.split(" ");
			this.addFormator(parts[0], parts[1]);
		}
		
		parts.add(new GeneratePart() {
			@Override
			public String methodPrefix() {
				return "GetIn";
			}
			@Override
			public String nullHandle(String cacheName, String indexName, String fieldNames) {
				return String.format("throw new DesignNotFoundException(\"%s.getIn%s\", %s);", cacheName, indexName, fieldNames);
			}
		});
		parts.add(new GeneratePart() {
			@Override
			public String methodPrefix() {
				return "FindIn";
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
		for(File file : designFolder.listFiles()) {
			try {
				List<DesignFile> fileMetas = designReader.read(file);
				for(DesignFile fileMeta : fileMetas) {
					DesignClassMeta classMeta = DesignClassMeta.build(fileMeta, "", "");
					if(!this.isExport(classMeta, exportType)) {
						continue;
					}
					String classContent = this.genCode(classMeta);
					
					FileUtil.writeToFile(outputPath, classMeta.getCacheName() + ".cs", classContent);
					System.out.println("生成" + classMeta.getCacheName());
				}
			} catch (Exception e) {
				System.out.println(file.getName() + "生成异常");
				e.printStackTrace();
			}
		}
		System.out.println("生成结束");
	}
	
	private void addFormator(String designType, String classType) {
		formatters.put(designType, classType);
		formatters.put(designType + "[]", listDeclare(classType));
	}
	
	public String genCode(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(String using : gen.getUsings()) {
			builder.NFormat("%s", using);
		}
		builder.NFormat("");
		builder.NFormat("%s {", gen.getNamespace());
		builder.NTFormat("public partial class %s : AbstractDesignCache<%s> {", classMeta.getCacheName(), formatClassName(classMeta));
		builder.append(genCustom(classMeta));
		builder.append(genFileName(classMeta));
		
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
		
		builder.NTFormat("}");
		builder.append(genEntityClass(classMeta));
		builder.NFormat("}");
		return builder.toString();
	}
	
	protected String genCustom(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(String custom : gen.getCustoms()) {
			builder.NFormat(custom.replaceAll("%s", classMeta.getCacheName()));
		}
		return builder.toString();
	}
	
//		CodeBuilder builder = new CodeBuilder();
//		builder.NTTFormat("private static %s instance;", classMeta.getCacheName());
//		builder.NTTFormat("public static %s Instance {", classMeta.getCacheName());
//		builder.NTTTFormat("get {");
//		builder.NTTTTFormat("if(instance == null) {");
//		builder.NTTTTTFormat("instance = new();");
//		builder.NTTTTTFormat("TextAsset textAsset = Resources.Load<TextAsset>(\"config/\" + instance.FileName);");
//		builder.NTTTTTFormat("instance.LoadData(textAsset.text, new X1SeparatorDesignParser());");
//		builder.NTTTTFormat("}");
//		builder.NTTTTFormat("return instance;");
//		builder.NTTTFormat("}");
//		builder.NTTFormat("}");
//		return builder.toString();
	
	private String genFileName(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTTFormat("public override string FileName {");
		builder.NTTTFormat("get {return \"%s\";}", classMeta.getFileName());
		builder.NTTFormat("}");
		return builder.toString();
	}
	
	private String genEntityClass(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public partial class %s : IDesignData {", formatClassName(classMeta));
		for(DesignColumnMeta columnMeta : classMeta.getColumnMetas()) {
			builder.NTTFormat("//%s", columnMeta.getComment());
			String typeExplain = typeExplain(columnMeta.getType());
			builder.NTTFormat("protected %s %s;", typeExplain, columnMeta.getName());
		}
		//写getId函数
		builder.NTTFormat("public int Id() {");
		builder.NTTTFormat("return %s;", classMeta.getIdDefine());
		builder.NTTFormat("}");
		//写getter
		for(DesignColumnMeta columnMeta : classMeta.getColumnMetas()) {
			String upperFieldName = DesignClassMeta.upperFirst(columnMeta.getName());
			String typeExplain = typeExplain(columnMeta.getType());
			
			builder.NTTFormat("public %s get%s() {", typeExplain, upperFieldName);
			builder.NTTTFormat("return %s;", columnMeta.getName());
			builder.NTTFormat("}");
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private String genIndexDefine(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String structDefine = formatIndexStruct(classMeta, indexMeta, 0);
			builder.NTTFormat("protected %s %s;", structDefine, indexMeta.getName());
		}
		return builder.toString();
	}
	
	private String genCollectorDefine(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String structDefine = formatCollectorStruct(classMeta, collectorMeta, 0);
			builder.NTTFormat("protected %s %s;", structDefine, collectorMeta.getName());
		}
		return builder.toString();
	}
	
	private String genLoadAutoGenerate(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTTFormat("protected override void LoadAutoGenerate() {");
		builder.append(genIndexFill(classMeta));
		builder.append(genCollectorFill(classMeta));
		builder.append(genConstFill(classMeta));
		builder.NTTFormat("}");
		return builder.toString();
	}
	
	@Override
	protected String genIndexFill(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String structDefine = formatIndexStruct(classMeta, indexMeta, 0);
			builder.NTTTFormat("//构建索引%s", indexMeta.getName());
			builder.NTTTFormat("%s %s = new %s();", structDefine, indexMeta.getName(), structDefine);
			builder.NTTTFormat("foreach(%s data in All) {", formatClassName(classMeta));
			
			String currLayerMap = indexMeta.getName();
			
			for(int i = 0; i < indexMeta.getFields().size(); i++) {
				String fieldName = indexMeta.getFields().get(i);
				String upperFieldName = DesignClassMeta.upperFirst(fieldName);
				
				if(i < indexMeta.getFields().size() - 1) {
					String subStruct = formatIndexSubStruct(classMeta, indexMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTTTFormat("bool hasLayer%sMap = %s.TryGetValue(data.get%s(), out %s %s);", (i + 1), currLayerMap, upperFieldName, subStruct, layerMap);
					builder.NTTTTFormat("if(!hasLayer%sMap) {", (i + 1));
					builder.NTTTTTFormat("%s = new %s();", layerMap, subStruct);
					builder.NTTTTTFormat("%s.Add(data.get%s(), %s);", currLayerMap, upperFieldName, layerMap);
					builder.NTTTTFormat("}");
					currLayerMap = layerMap;
				} else {
					//最后一层是存数据了
					builder.NTTTTFormat("%s.Add(data.get%s(), data);", currLayerMap, upperFieldName);
				}
			}
			builder.NTTTFormat("}");
			builder.NTTTFormat("this.%s = %s;", indexMeta.getName(), indexMeta.getName());
		}
		return builder.toString();
	}
	
	@Override
	protected String genCollectorFill(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String structDefine = formatCollectorStruct(classMeta, collectorMeta, 0);
			builder.NTTTFormat("//构建索引%s", collectorMeta.getName());
			builder.NTTTFormat("%s %s = new %s();", structDefine, collectorMeta.getName(), structDefine);
			builder.NTTTFormat("foreach(%s data in All) {", formatClassName(classMeta));
			
			String currLayerMap = collectorMeta.getName();
			
			for(int i = 0; i < collectorMeta.getFields().size(); i++) {
				String fieldName = collectorMeta.getFields().get(i);
				String upperFieldName = DesignClassMeta.upperFirst(fieldName);
				
				if(i < collectorMeta.getFields().size() - 1) {
					String subStruct = formatCollectorSubStruct(classMeta, collectorMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTTTFormat("bool hasLayer%sMap = %s.TryGetValue(data.get%s(), out %s %s);", (i + 1), currLayerMap, upperFieldName, subStruct, layerMap);
					builder.NTTTTFormat("if(!hasLayer%sMap) {", (i + 1));
					builder.NTTTTTFormat("%s = new %s();", layerMap, subStruct);
					builder.NTTTTTFormat("%s.Add(data.get%s(), %s);", currLayerMap, upperFieldName, layerMap);
					builder.NTTTTFormat("}");
					currLayerMap = layerMap;
				} else {
					builder.NTTTTFormat("bool hasCollector = %s.TryGetValue(data.get%s(), out System.Collections.Generic.List<%s> collector);", currLayerMap, upperFieldName, formatClassName(classMeta));
					builder.NTTTTFormat("if(!hasCollector) {");
					builder.NTTTTTFormat("collector = new System.Collections.Generic.List<%s>();", formatClassName(classMeta));
					builder.NTTTTTFormat("%s.Add(data.get%s(), collector);", currLayerMap, upperFieldName);
					builder.NTTTTFormat("}");
					builder.NTTTTFormat("collector.Add(data);");
				}
			}
			builder.NTTTFormat("}");
			builder.NTTTFormat("this.%s = %s;", collectorMeta.getName(), collectorMeta.getName());
		}
		return builder.toString();
	}

	@Override
	protected String mapDeclare() {
		return "System.Collections.Generic.Dictionary<#key#, #value#>";
	}

	@Override
	protected String listDeclare(String elementType) {
		return "System.Collections.Generic.List<" + elementType + ">";
	}

	@Override
	protected String typeExplain(String typeName) {
		return formatters.getOrDefault(typeName, typeName);
	}


	@Override
	protected String genIndexMethod(DesignClassMeta classMeta, GeneratePart part) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String upperIndexName = DesignClassMeta.upperFirst(indexMeta.getName());
			builder.NTTFormat("public %s %s%s(", formatClassName(classMeta), part.methodPrefix(), upperIndexName); 
			
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
				builder.format("%s %s", typeExplain, columnMeta.getName());
				fieldNames += columnMeta.getName();
				firstParam = false;
			}
			builder.append(") {");
			
			String currMap = indexMeta.getName();
			for(int i = 0; i < indexMeta.getFields().size(); i++) {
				String field = indexMeta.getFields().get(i);
				if(i < indexMeta.getFields().size() - 1) {
					String subStruct = formatIndexSubStruct(classMeta, indexMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					builder.NTTTFormat("bool hasLayer%sMap = %s.TryGetValue(%s, out %s %s);", (i + 1), currMap, field, subStruct, layerMap);
					builder.NTTTFormat("if(!hasLayer%sMap) {", (i + 1));
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTTFormat("}");
					currMap = layerMap;
				} else {
					builder.NTTTFormat("bool hasIndex = %s.TryGetValue(%s, out %s t);", currMap, field, formatClassName(classMeta));
					builder.NTTTFormat("if(!hasIndex) {");
					builder.NTTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTTFormat("}");
					builder.NTTTFormat("return t;");
				}
			}
			builder.NTTFormat("}");
		}
		return builder.toString();
	}

	@Override
	protected String genCollectorMethod(DesignClassMeta classMeta, GeneratePart part) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String upperIndexName = DesignClassMeta.upperFirst(collectorMeta.getName());
			builder.NTTFormat("public System.Collections.Generic.List<%s> %s%s(", formatClassName(classMeta), part.methodPrefix(), upperIndexName);
			
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
				builder.format("%s %s", typeExplain, columnMeta.getName());
				fieldNames += columnMeta.getName();
				firstParam = false;
			}
			builder.append(") {");
			
			String currMap = collectorMeta.getName();
			for(int i = 0; i < collectorMeta.getFields().size(); i++) {
				String field = collectorMeta.getFields().get(i);
				if(i < collectorMeta.getFields().size() - 1) {
					String subStruct = formatCollectorSubStruct(classMeta, collectorMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTTFormat("bool hasLayer%sMap = %s.TryGetValue(%s, out %s %s);", (i + 1), currMap, field, subStruct, layerMap);
					builder.NTTTFormat("if(!hasLayer%sMap) {", (i + 1));
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTTFormat("}");
					currMap = layerMap;
				} else {
					builder.NTTTFormat("bool hasCollector = %s.TryGetValue(%s, out System.Collections.Generic.List<%s> ts);", currMap, field, formatClassName(classMeta));
					builder.NTTTFormat("if(!hasCollector) {");
					builder.NTTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTTFormat("}");
					builder.NTTTFormat("return ts;");
				}
			}
			builder.NTTFormat("}");
		}
		return builder.toString();
	}
	
	private String genConstFields(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder(CodeBuilder.TT);
		for(DesignConstData constData : classMeta.getConstDatas()) {
			String typeExplain = typeExplain(constData.getType());
			String upperKeyName = DesignClassMeta.upperFirst(constData.getKey());
			builder.NFormat("//%s", constData.getComment());
			builder.NFormat("protected %s %s;", typeExplain, constData.getKey());
			builder.NFormat("public %s get%s() {", typeExplain, upperKeyName);
			builder.NTFormat("return %s;", constData.getKey());
			builder.NFormat("}");
		}
		return builder.toString();
	}
	
	private String genConstFill(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder(CodeBuilder.TTT);
		if(classMeta.getConstDatas().isEmpty()) {
			return builder.toString();
		}
		String upperKeyName = DesignClassMeta.upperFirst(classMeta.getConstKeyName());
		String upperValueName = DesignClassMeta.upperFirst(classMeta.getConstValueName());
		builder.NFormat("//构建常量");
		builder.NFormat("System.Collections.Generic.Dictionary<string, string> constMap = new System.Collections.Generic.Dictionary<string, string>();");
		builder.NFormat("foreach(%s data in All) {", formatClassName(classMeta));
		builder.NTFormat("constMap.Add(data.get%s(), data.get%s());", upperKeyName, upperValueName);
		builder.NFormat("}");
		builder.NFormat("this.LoadConst(constMap);");
		return builder.toString();
	}
	
	public String formatClassName(DesignClassMeta classMeta) {
		return String.format(gen.getClassname(), classMeta.getRawClassName());
	}
}
