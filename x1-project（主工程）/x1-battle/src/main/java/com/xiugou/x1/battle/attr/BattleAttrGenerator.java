/**
 * 
 */
package com.xiugou.x1.battle.attr;

import org.gaming.tool.CodeBuilder;
import org.gaming.tool.FileUtil;

/**
 * @author YY
 *
 */
public class BattleAttrGenerator {
	
	private static String interfaceName = "IBattleAttr";
	private static String className = "BattleAttr";
	
	private static String pbJavaPackage = "pb.xiugou.x1.protobuf.battleattr";
	private static String pbFileName = "BattleAttr";
	private static String pbClassName = "PbBattleAttr";
	private static String pbClassFullName = pbJavaPackage + "." + pbFileName + "." + pbClassName;
	
	private static String genPackage = "com.xiugou.x1.battle.attr";
	private static String attrEnumIns = "AttrEnumIns";
	private static String spriteFullName = "com.xiugou.x1.battle.sprite.Sprite";
	
	private static String pbCsharpPackage = "pb.xiugou.x1.protobuf";
	
	/**
	 * 使用方法
	 * 1、打印protobuf属性类
	 * 2、生成protobuf类文件（使用协议导出bat）
	 * 3、打印接口代码并覆盖
	 * 4、打印实现类代码并覆盖
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String path = "D:\\Git\\x1-server\\x1-project\\x1-battle\\src\\main\\java\\com\\xiugou\\x1\\battle\\attr";
		
		//打印protobuf属性类
		printPbBattleAttr("D:\\Git\\x1-protofiles\\src");
		//打印接口代码
		printBattleAttrInterface(path);
		//打印实现类代码
		printBattleAttrClass(path);
		writeAttrEnum(path);
		//
		System.out.println("完成");
	}
	
	public static void printPbBattleAttr(String fileDir) {
		CodeBuilder builder = new CodeBuilder();
		builder.append("syntax = \"proto3\";");
		builder.NFormat("package pb;");
		builder.NFormat("option java_package = \"%s\";", pbJavaPackage);
		builder.NFormat("option csharp_namespace = \"%s\";", pbCsharpPackage);
		builder.NFormat("");
		builder.NFormat("message %s {", pbClassName);
		int index = 1;
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTFormat("int64 %s = %s;\t//%s", attr.name(), index, attr.getDesc());
			index += 1;
		}
		builder.NFormat("}");
		
		FileUtil.writeToFile(fileDir, pbFileName + ".proto", builder.toString());
	}
	
	public static String printBuildPbBattleAttr() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public %s build() {", pbClassFullName);
		builder.NTTFormat("%s.Builder builder = %s.newBuilder();", pbClassFullName, pbClassFullName);
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
			builder.NTTFormat("builder.set%s(this.%s);", upAttrName, attrName);
		}
		builder.NTTFormat("return builder.build();");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	
	protected static void printBattleAttrInterface(String fileDir) {
		CodeBuilder builder = new CodeBuilder();
		builder.NFormat("package %s;", genPackage);
		builder.NFormat("");
		builder.NFormat("");
		builder.NFormat("public interface %s {", interfaceName);
		
		builder.append(writeInterfaceMerge());
		builder.append(writeInterfaceTopMerge());
		builder.append(writeInterfaceAttrMap());
		builder.append(writeInterfaceGetter());
		builder.append(writeInterfaceSetter());
		builder.append(writeInterfaceMultiple());
		builder.append(writeInterfaceDmultiple());
		builder.append(writeInterfaceAdd());
		builder.append(writeInterfaceAddById());
		builder.append(writeInterfaceSubById());
		builder.append(writeInterfaceGet());
		builder.append(writeInterfaceIsEqual());
		
		builder.NFormat("}");
		
		FileUtil.writeToFile(fileDir, interfaceName + ".java", builder.toString());
	}
	
	protected static String writeInterfaceGet() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default long get(String attrName) {");
		builder.NTTFormat("return 0;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	
	protected static void printBattleAttrClass(String fileDir) {
		CodeBuilder builder = new CodeBuilder();
		
		builder.NFormat("package %s;", genPackage);
		builder.NFormat("");
		builder.NFormat("");
		builder.NFormat("public class %s implements %s {", className, interfaceName);
		
		builder.append(writeFields());
		builder.append(writeMerge());
		builder.append(writeTopMerge());
		builder.append(writeClear());
		builder.append(writeAttrMap());
		builder.append(writeCopy(className));
		builder.append(writeICopy(className));
		builder.append(writeNotZeroAttrMap(className));
		builder.append(writeMultiple(className));
		builder.append(writeDmultiple(className));
		builder.append(writeAdd(className));
		builder.append(writeAddById(className));
		builder.append(writeSubById(className));
		builder.append(writeGet(className));
		builder.append(writeIsEqual(className));
		
		builder.append(writeGetterAndSetter());
		builder.append(printBuildPbBattleAttr());
		builder.NFormat("}");
		
		FileUtil.writeToFile(fileDir, className + ".java", builder.toString());
	}
	
	
	private static String writeFields() {
		CodeBuilder builder = new CodeBuilder();
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTFormat("private long %s;//%s", attr.name(), attr.getDesc());
		}
		return builder.toString();
	}
	
	private static String writeGetterAndSetter() {
		CodeBuilder builder = new CodeBuilder();
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0,1).toUpperCase() + attrName.substring(1);
			//getter
			builder.NTFormat("public long get%s() {", upAttrName);
			builder.NTTFormat("return %s;", attrName);
			builder.NTFormat("}");
			
			//setter
			builder.NTFormat("public void set%s(long %s) {", upAttrName, attrName);
			builder.NTTFormat("this.%s = %s;", attrName, attrName);
			builder.NTFormat("}");
		}
		return builder.toString();
	}
	
	private static String writeInterfaceMerge() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default void merge(%s attr) {", interfaceName);
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceTopMerge() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default void topMerge(%s attr) {", interfaceName);
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceAttrMap() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default java.util.Map<String, Long> attrMap() {");
		builder.NTTFormat("return null;");
		builder.NTFormat("}");
		builder.NTFormat("default java.util.Map<String, Long> notZeroAttrMap() {");
		builder.NTTFormat("return null;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceGetter() {
		CodeBuilder builder = new CodeBuilder();
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0,1).toUpperCase() + attrName.substring(1);
			//getter
			builder.NTFormat("/**");
			builder.NTFormat(" * %s", attr.getDesc());
			builder.NTFormat(" */");
			builder.NTFormat("default long get%s() {", upAttrName);
			builder.NTTFormat("return 0;");
			builder.NTFormat("}");
		}
		return builder.toString();
	}
	
	private static String writeInterfaceSetter() {
		CodeBuilder builder = new CodeBuilder();
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0,1).toUpperCase() + attrName.substring(1);
			//setter
			builder.NTFormat("/**");
			builder.NTFormat(" * %s", attr.getDesc());
			builder.NTFormat(" */");
			builder.NTFormat("default void set%s(long %s) {", upAttrName, attrName);
			builder.NTFormat("}");
		}
		return builder.toString();
	}
	
	private static String writeInterfaceMultiple() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default void multiple(int multiple) {");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceDmultiple() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default void dmultiple(double multiple) {");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceAdd() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default void add(String attrName, long value) {");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceAddById() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default void addById(int attrId, long value) {");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceSubById() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default void subById(int attrId, long value) {");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeInterfaceIsEqual() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("default boolean isEqual(IBattleAttr attr) {");
		builder.NTTFormat("return false;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeMerge() {
		CodeBuilder builder = new CodeBuilder();
		String lowAttrName = className.substring(0,1).toLowerCase() + className.substring(1);
		
		builder.NTFormat("@Override");
		builder.NTFormat("public void merge(%s %s) {", interfaceName, lowAttrName);
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0,1).toUpperCase() + attrName.substring(1);
			builder.NTTFormat("this.%s += %s.get%s();", attrName, lowAttrName, upAttrName);
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeTopMerge() {
		CodeBuilder builder = new CodeBuilder();
		String lowAttrName = className.substring(0,1).toLowerCase() + className.substring(1);
		
		builder.NTFormat("@Override");
		builder.NTFormat("public void topMerge(%s %s) {", interfaceName, lowAttrName);
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0,1).toUpperCase() + attrName.substring(1);
			builder.NTTFormat("if(%s.get%s() > this.%s) {", lowAttrName, upAttrName, attrName);
			builder.NTTTFormat("this.%s = %s.get%s();", attrName, lowAttrName, upAttrName);
			builder.NTTFormat("}");
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	
	private static String writeClear() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public void clear() {");
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("this.%s = 0;", attr.name());
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeAttrMap() {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public java.util.Map<String, Long> attrMap() {");
		builder.NTTFormat("java.util.Map<String, Long> map = new java.util.HashMap<>();");
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("map.put(\"%s\", %s);", attr.name(), attr.name());
		}
		builder.NTTFormat("return map;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeCopy(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public %s copy() {", className);
		builder.NTTFormat("%s copy = new %s();", className, className);
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("copy.%s = this.%s;", attr.name(), attr.name());
		}
		builder.NTTFormat("return copy;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	
	private static String writeICopy(String className) {
		String lowAttrName = className.substring(0, 1).toLowerCase() + className.substring(1);
		
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public static %s icopy(%s %s) {", className, interfaceName, lowAttrName);
		builder.NTTFormat("%s copy = new %s();", className, className);
		for (BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
			builder.NTTFormat("copy.%s = %s.get%s();", attr.name(), lowAttrName, upAttrName);
		}
		builder.NTTFormat("return copy;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeNotZeroAttrMap(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("public java.util.Map<String, Long> notZeroAttrMap() {");
		builder.NTTFormat("java.util.Map<String, Long> map = new java.util.LinkedHashMap<>();");
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("if(%s > 0) {", attr.name());
			builder.NTTTFormat("map.put(\"%s\", %s);", attr.name(), attr.name());
			builder.NTTFormat("}");
		}
		builder.NTTFormat("return map;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeMultiple(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("public void multiple(int multiple) {");
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("this.%s *= multiple;", attr.name());
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeDmultiple(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("public void dmultiple(double multiple) {");
		for (BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("this.%s = (long)(this.%s * multiple);", attr.name(), attr.name());
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeGet(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("public long get(String attrName) {");
		for (BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("if(\"%s\".equals(attrName)) {", attr.name());
			builder.NTTTFormat("return this.%s;", attr.name());
			builder.NTTFormat("}");
		}
		builder.NTTFormat("return 0;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeAdd(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("public void add(String attrName, long value) {");
		for (BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("if(\"%s\".equals(attrName)) {", attr.name());
			builder.NTTTFormat("this.%s += value;", attr.name());
			builder.NTTFormat("}");
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeAddById(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("public void addById(int attrId, long value) {");
		for (BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("if(attrId == %s) {", attr.getId());
			builder.NTTTFormat("this.%s += value;", attr.name());
			builder.NTTFormat("}");
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	private static String writeSubById(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("public void subById(int attrId, long value) {");
		for (BattleAttrEnum attr : BattleAttrEnum.values()) {
			builder.NTTFormat("if(attrId == %s) {", attr.getId());
			builder.NTTTFormat("this.%s -= value;", attr.name());
			builder.NTTFormat("}");
		}
		builder.NTFormat("}");
		return builder.toString();
	}
	
	
	private static String writeIsEqual(String className) {
		CodeBuilder builder = new CodeBuilder();
		builder.NTFormat("@Override");
		builder.NTFormat("public boolean isEqual(IBattleAttr attr) {");
		for (BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
			builder.NTTFormat("if(this.%s != attr.get%s()) {", attr.name(), upAttrName);
			builder.NTTTFormat("return false;");
			builder.NTTFormat("}");
		}
		builder.NTTFormat("return true;");
		builder.NTFormat("}");
		return builder.toString();
	}
	
	protected static void writeAttrEnum(String fileDir) {
		CodeBuilder builder = new CodeBuilder();
		builder.NFormat("package %s;", genPackage);
		builder.NFormat("");
		builder.NFormat("import %s;", spriteFullName);
		builder.NFormat("");
		builder.NFormat("public interface %s {", attrEnumIns);
		
		for(BattleAttrEnum attr : BattleAttrEnum.values()) {
			String attrName = attr.name();
			String upAttrName = attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
			builder.NTFormat("public static final AttrEnum %s = new AttrEnum(%s, \"%s\") {", attrName, attr.getId(), attrName);
			builder.NTTFormat("public long getAttrValue(Sprite sprite) {");
			builder.NTTTFormat("return sprite.getBattleAttr().get%s();", upAttrName);
			builder.NTTFormat("}");
			builder.NTFormat("};");
		}
		
		builder.NFormat("}");
		
		FileUtil.writeToFile(fileDir, attrEnumIns + ".java", builder.toString());
	}
	
}
