/**
 * 
 */
package org.gaming.design.gen.java;

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
public class JavaDesignTemplate extends GenerateTemplate {

	private JavaGen gen;
	private Map<String, String> formatters = new HashMap<>();
	
	public JavaDesignTemplate(String genFilePath) {
		this.gen = JavaGen.readGenFile(genFilePath);
		this.addFormator("int", "int");
		this.addFormator("long", "long");
		this.addFormator("string", "String");
		this.addFormator("float", "float");
		this.addFormator("lang", "String");
		
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
				return String.format("throw new org.gaming.design.exception.DesignNotFoundException(\"%s.getIn%s\", %s);", cacheName, indexName, fieldNames);
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
	
	private void addFormator(String designType, String classType) {
		formatters.put(designType, classType);
		if("int".equals(classType)) {
			formatters.put(designType + "[]", listDeclare("Integer"));
		} else if("long".equals(classType)) {
			formatters.put(designType + "[]", listDeclare("Long"));
		} else if("float".equals(classType)) {
			formatters.put(designType + "[]", listDeclare("Float"));
		} else {
			formatters.put(designType + "[]", listDeclare(classType));
		}
	}
	
	@Override
	public void gen(String outputPath, String exportSourcePath, IDesignReader designReader, ExportType exportType) {
		String abstractOutputPath = outputPath + File.separator + "autogen";
		
		File designFolder = new File(exportSourcePath);
		for(File file : designFolder.listFiles()) {
			try {
				List<DesignFile> fileMetas = designReader.read(file);
				
				for(DesignFile fileMeta : fileMetas) {
					DesignClassMeta classMeta = DesignClassMeta.build(fileMeta, "", "");
					if(!this.isExport(classMeta, exportType)) {
						continue;
					}
					//每次生成代码都需要生成基类
					String abstractFileContent = this.genAbstractCacheClass(classMeta);
					FileUtil.writeToFile(abstractOutputPath, classMeta.getAbstractName() + ".java", abstractFileContent);
					System.out.println("生成" + classMeta.getAbstractName());
					
					//子类文件如果已经存在则不进行生成
					File oriJavaFile = new File(outputPath + File.separator + classMeta.getCacheName() + ".java");
					if(oriJavaFile.exists()) {
						continue;
					}
					String fileContent = this.genCacheClass(classMeta);
					FileUtil.writeToFile(outputPath, classMeta.getCacheName() + ".java", fileContent);
					System.out.println("生成" + classMeta.getCacheName());
				}
			} catch (Exception e) {
				System.out.println(file.getName() + "生成异常");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected String mapDeclare() {
		throw new UnsupportedOperationException("不该被调用的函数");
	}

	@Override
	protected String listDeclare(String elementType) {
		return "java.util.List<" + elementType + ">";
	}

	@Override
	protected String genIndexMethod(DesignClassMeta classMeta, GeneratePart part) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String upperIndexName = DesignClassMeta.upperFirst(indexMeta.getName());
			builder.NTFormat("public final T %s%s(", part.methodPrefix(), upperIndexName); 
			
			//写函数签名、参数列表
			boolean firstParam = true;
			String fieldNames = "";
			for(String field : indexMeta.getFields()) {
				if(!firstParam) {
					builder.append(", ");
					fieldNames += ", ";
				}
				DesignColumnMeta columnMeta = classMeta.nameToColumns().get(field);
				String typeExplain = typeWrapperExplain(columnMeta.getType(), false);
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
					builder.NTTFormat("%s %s = %s.get(%s);", subStruct, layerMap, currMap, field);
					builder.NTTFormat("if(%s == null) {", layerMap);
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					currMap = layerMap;
				} else {
					builder.NTTFormat("T t = %s.get(%s);", currMap, field);
					builder.NTTFormat("if(t == null) {");
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					builder.NTTFormat("return t;", currMap, field);
				}
			}
			builder.NTFormat("}");
		}
		return builder.toString();
	}

	@Override
	protected String genCollectorMethod(DesignClassMeta classMeta, GeneratePart part) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String upperIndexName = DesignClassMeta.upperFirst(collectorMeta.getName());
			builder.NTFormat("public final java.util.ArrayList<T> %s%s(", part.methodPrefix(), upperIndexName);
			
			//写函数签名、参数列表
			boolean firstParam = true;
			String fieldNames = "";
			for(String field : collectorMeta.getFields()) {
				if(!firstParam) {
					builder.append(", ");
					fieldNames += ", ";
				}
				DesignColumnMeta columnMeta = classMeta.nameToColumns().get(field);
				String typeExplain = typeWrapperExplain(columnMeta.getType(), false);
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
					builder.NTTFormat("%s %s = %s.get(%s);", subStruct, layerMap, currMap, field);
					builder.NTTFormat("if(%s == null) {", layerMap);
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					currMap = layerMap;
				} else {
					builder.NTTFormat("java.util.ArrayList<T> ts = %s.get(%s);", currMap, field);
					builder.NTTFormat("if(ts == null) {");
					builder.NTTTFormat(part.nullHandle(classMeta.getCacheName(), upperIndexName, fieldNames));
					builder.NTTFormat("}");
					builder.NTTFormat("return ts;");
				}
			}
			builder.NTFormat("}");
		}
		return builder.toString();
	}

	@Override
	protected String typeExplain(String typeName) {
		return formatters.getOrDefault(typeName, typeName);
	}
	
	protected String typeWrapperExplain(String typeName, boolean useWrapper) {
		if("int".equals(typeName)) {
			if(useWrapper) {
				return "Integer";
			} else {
				return formatters.getOrDefault(typeName, typeName);
			}
		} else if("long".equals(typeName)) {
			if(useWrapper) {
				return "Long";
			} else {
				return formatters.getOrDefault(typeName, typeName);
			}
		} else if("float".equals(typeName)) {
			if(useWrapper) {
				return "Float";
			} else {
				return formatters.getOrDefault(typeName, typeName);
			}
		} else {
			return formatters.getOrDefault(typeName, typeName);
		}
	}
	
	public String genCacheClass(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.format("%s", gen.getPackge());
		builder.NFormat("");
		builder.NFormat("");
		builder.NFormat("import %s.%s;", gen.getPackge().replace("package", "").replace(";", ".autogen").trim(), classMeta.getAbstractName());
		builder.NFormat("");
		
		builder.NFormat("public class %s extends %s<%s.%s> {", classMeta.getCacheName(), classMeta.getAbstractName(),
				classMeta.getAbstractName(), formatClassName(classMeta));
		builder.NFormat("}");
		return builder.toString();
	}
	
	public String genAbstractCacheClass(DesignClassMeta classMeta) {
		String classFullName = classMeta.getAbstractName() + "." + formatClassName(classMeta);
		
		CodeBuilder builder = new CodeBuilder();
		builder.format("%s", gen.getPackge().replace(";", ".autogen;"));
		builder.NFormat("");
		builder.NFormat("");
		builder.NFormat("public abstract class %s<T extends %s> extends org.gaming.design.loader.DesignCache<T> {", classMeta.getAbstractName(), classFullName);
		builder.NTFormat("@Override");
		builder.NTFormat("public String fileName() {");
		builder.NTTFormat("return \"%s\";", classMeta.getFileName());
		builder.NTFormat("}");
		
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
		builder.append(genEntityClass(classMeta));
		builder.NFormat("");
		builder.append(genConstFields(classMeta));
		
		builder.NFormat("}");
		return builder.toString();
	}
	
	private String genIndexDefine(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String structDefine = formatIndexStruct(classMeta, indexMeta, 0);
			builder.NTFormat("protected %s %s;", structDefine, indexMeta.getName());
		}
		return builder.toString();
	}
	
	private String genCollectorDefine(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignCollectorMeta collectorMeta : classMeta.getCollectorMetas()) {
			String structDefine = formatCollectorStruct(classMeta, collectorMeta, 0);
			builder.NTFormat("protected %s %s;", structDefine, collectorMeta.getName());
		}
		return builder.toString();
	}
	
	private String genLoadAutoGenerate(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("protected final void loadAutoGenerate() {");
		
		builder.append(genIndexFill(classMeta));
		builder.append(genCollectorFill(classMeta));
		builder.append(genConstFill(classMeta));
		
		builder.NTFormat("}");
		return builder.toString();
	}
	
	protected String genIndexFill(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignIndexMeta indexMeta : classMeta.getIndexMetas()) {
			String structDefine = formatIndexStruct(classMeta, indexMeta, 0);
			builder.NTTFormat("//构建索引%s", indexMeta.getName());
			builder.NTTFormat("%s %s = new %s();", structDefine, indexMeta.getName(), structDefine);
			builder.NTTFormat("for(T data : all()) {");
			
			String currLayerMap = indexMeta.getName();
			
			//最后一层是存数据了
			for(int i = 0; i < indexMeta.getFields().size(); i++) {
				String fieldName = indexMeta.getFields().get(i);
				String upperFieldName = DesignClassMeta.upperFirst(fieldName);
				
				if(i < indexMeta.getFields().size() - 1) {
					String subStruct = this.formatIndexSubStruct(classMeta, indexMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTTFormat("%s %s = %s.get(data.get%s());", subStruct, layerMap, currLayerMap, upperFieldName);
					builder.NTTTFormat("if(%s == null) {", layerMap);
					builder.NTTTTFormat("%s = new %s();", layerMap, subStruct);
					builder.NTTTTFormat("%s.put(data.get%s(), %s);", currLayerMap, upperFieldName, layerMap);
					builder.NTTTFormat("}");
					currLayerMap = layerMap;
				} else {
					builder.NTTTFormat("%s.put(data.get%s(), data);", currLayerMap, upperFieldName);
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
			builder.NTTFormat("//构建索引%s", collectorMeta.getName());
			builder.NTTFormat("%s %s = new %s();", structDefine, collectorMeta.getName(), structDefine);
			builder.NTTFormat("for(T data : all()) {");
			
			String currLayerMap = collectorMeta.getName();
			
			for(int i = 0; i < collectorMeta.getFields().size(); i++) {
				String fieldName = collectorMeta.getFields().get(i);
				String upperFieldName = DesignClassMeta.upperFirst(fieldName);
				
				if(i < collectorMeta.getFields().size() - 1) {
					String subStruct = formatCollectorSubStruct(classMeta, collectorMeta, i + 1);
					String layerMap = "layer" + (i + 1) + "Map";
					
					builder.NTTTFormat("%s %s = %s.get(data.get%s());", subStruct, layerMap, currLayerMap, upperFieldName);
					builder.NTTTFormat("if(%s == null) {", layerMap);
					builder.NTTTTFormat("%s = new %s();", layerMap, subStruct);
					builder.NTTTTFormat("%s.put(data.get%s(), %s);", currLayerMap, upperFieldName, layerMap);
					builder.NTTTFormat("}");
					currLayerMap = layerMap;
				} else {
					builder.NTTTFormat("java.util.ArrayList<T> collector = %s.get(data.get%s());", currLayerMap, upperFieldName);
					builder.NTTTFormat("if(collector == null) {");
					builder.NTTTTFormat("collector = new java.util.ArrayList<>();");
					builder.NTTTTFormat("%s.put(data.get%s(), collector);", currLayerMap, upperFieldName);
					builder.NTTTFormat("}");
					builder.NTTTFormat("collector.add(data);");
				}
			}
			builder.NTTFormat("}");
			builder.NTTFormat("this.%s = %s;", collectorMeta.getName(), collectorMeta.getName());
		}
		return builder.toString();
	}
	
	private String genEntityClass(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public static class %s implements org.gaming.design.loader.IDesignData {", formatClassName(classMeta));
		for(DesignColumnMeta columnMeta : classMeta.getColumnMetas()) {
			
			builder.NTTFormat("/**");
			builder.NTTFormat(" * %s", columnMeta.getComment());
			builder.NTTFormat(" */");
			
			String typeExplain = typeExplain(columnMeta.getType());
			
			builder.NTTFormat("protected %s %s;", typeExplain, columnMeta.getName());
		}
		//写getId函数
		builder.NTTFormat("@Override");
		builder.NTTFormat("public int id() {");
		builder.NTTTFormat("return %s;", classMeta.getIdDefine());
		builder.NTTFormat("}");
		
		//写getter
		for(DesignColumnMeta columnMeta : classMeta.getColumnMetas()) {
			String upperFieldName = DesignClassMeta.upperFirst(columnMeta.getName());
			
			String typeExplain = formatters.getOrDefault(columnMeta.getType(), columnMeta.getType());
			
			builder.NTTFormat("public %s get%s() {", typeExplain, upperFieldName);
			builder.NTTTFormat("return %s;", columnMeta.getName());
			builder.NTTFormat("}");
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private String genConstFields(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		for(DesignConstData constData : classMeta.getConstDatas()) {
			String typeExplain = typeExplain(constData.getType());
			String upperKeyName = DesignClassMeta.upperFirst(constData.getKey());
			
			builder.NTFormat("protected %s %s;", typeExplain, constData.getKey());
			builder.NTFormat("public %s get%s() {", typeExplain, upperKeyName);
			builder.NTTFormat("return %s;", constData.getKey());
			builder.NTFormat("}");
		}
		return builder.toString();
	}
	
	private String genConstFill(DesignClassMeta classMeta) {
		CodeBuilder builder = new CodeBuilder();
		if(classMeta.getConstDatas().isEmpty()) {
			return builder.toString();
		}
		String upperKeyName = DesignClassMeta.upperFirst(classMeta.getConstKeyName());
		String upperValueName = DesignClassMeta.upperFirst(classMeta.getConstValueName());
		builder.NTTFormat("//构建常量");
		builder.NTTFormat("java.util.HashMap<String, String> constMap = new java.util.HashMap<String, String>();");
		builder.NTTFormat("for(T data : all()) {");
		builder.NTTTFormat("constMap.put(data.get%s(), data.get%s());", upperKeyName, upperValueName);
		builder.NTTFormat("}");
		builder.NTTFormat("this.loadConst(constMap);");
		return builder.toString();
	}

	@Override
	protected String formatIndexStruct(DesignClassMeta classMeta, DesignIndexMeta indexMeta, int index) {
		if (index >= indexMeta.getFields().size() - 1) {
			String keyName = indexMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = typeWrapperExplain(keyMeta.getType(), true);
			return "java.util.HashMap<" + typeExplain + ", T>";
		} else {
			String keyName = indexMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = typeWrapperExplain(keyMeta.getType(), true);
			return "java.util.HashMap<" + typeExplain + ", " + formatIndexStruct(classMeta, indexMeta, index + 1) + ">";
		}
	}

	@Override
	protected String formatCollectorStruct(DesignClassMeta classMeta, DesignCollectorMeta collectorMeta, int index) {
		if (index >= collectorMeta.getFields().size() - 1) {
			String keyName = collectorMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = typeWrapperExplain(keyMeta.getType(), true);
			return "java.util.HashMap<" + typeExplain + ", java.util.ArrayList<T>>";
		} else {
			String keyName = collectorMeta.getFields().get(index);
			DesignColumnMeta keyMeta = classMeta.nameToColumns().get(keyName);
			String typeExplain = typeWrapperExplain(keyMeta.getType(), true);
			return "java.util.HashMap<" + typeExplain + ", " + formatCollectorStruct(classMeta, collectorMeta, index + 1) + ">";
		}
	}
	
	public String formatClassName(DesignClassMeta classMeta) {
		return String.format(gen.getClassname(), classMeta.getRawClassName());
	}
}
