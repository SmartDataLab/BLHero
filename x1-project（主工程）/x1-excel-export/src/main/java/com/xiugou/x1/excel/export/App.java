package com.xiugou.x1.excel.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.gaming.design.export.CsvExporter;
import org.gaming.design.export.DesignExporter;
import org.gaming.design.export.LanguageFileGenerator;
import org.gaming.design.loader.DesignFile;
import org.gaming.design.meta.ExportType;
import org.gaming.tool.FileUtil;


/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) {
//		args = new String[] {"2", "E:\\git\\dx1-config\\excel", "E:\\git\\dx1-config\\excel\\export\\", "server-csv", "csv", "S", "false"};
		args = new String[] {"5", "E:\\git\\dx1-server\\dx1-project\\dx1-design\\src\\main\\java\\com\\crwl\\dx1\\design\\module", "E:\\git\\dx1-config\\excel\\export\\server-csv", "E:\\git\\dx1-config\\java-gen.txt"};
		
		
		System.out.println("运行参数为：" + Arrays.toString(args));
		
		if(args == null || args.length < 1) {
			System.out.println("缺少运行参数");
			return;
		}
		String runType = args[0];
		
		if("1".equals(runType)) {
			if(args.length < 4) {
				System.out.println("缺少运行参数" + Arrays.toString(args));
				return;
			}
			String inputPath = args[1];
			String langFileName = args[2];
			String langClassName = args[3];
			//1、根据配置表导出语言表内容
			LanguageFileGenerator.genLangFile(inputPath, langFileName, langClassName);
		} else if("2".equals(runType)) {
			if(args.length < 5) {
				System.out.println("缺少运行参数" + Arrays.toString(args));
				return;
			}
			String excelHome = args[1];
			String exportHome = args[2];
			String outputFolder = args[3];
			String outputType = args[4];
			String exportType = args[5];
			String zipOrNot = args[6];
			//2、导出配置表数据
			Collection<DesignFile> files = DesignExporter.export(excelHome, exportHome + File.separator + outputFolder, outputType, ExportType.vof(exportType));
			export(files, exportHome + File.separator + outputFolder, outputType, ExportType.vof(exportType));
			
			if(Boolean.parseBoolean(zipOrNot)) {
				//全配置压缩打包
				File exportDir = new File(exportHome + File.separator + outputFolder);
				List<File> fileToZips = new ArrayList<>();
				for(File file : exportDir.listFiles()) {
					fileToZips.add(file);
				}
				FileUtil.zip(exportHome + File.separator + outputFolder + ".zip", fileToZips);
			}
			
			//文件分组与压缩生成清单文件，目前所有配置全部压缩也就400K，无需分组
//TOBE			listFiles(files, exportHome, outputFolder, outputType);
//			zipByGroup(exportHome, outputFolder, exportHome + File.separator + outputFolder + "-list");
		} else if("3".equals(runType)) {
			if(args.length < 4) {
				System.out.println("缺少运行参数" + Arrays.toString(args));
				return;
			}
			String genOutputPath = args[1];
			String configInputPath = args[2];
			String genFilePath = args[3];
			X1TypeScriptDesignGenerator.genCode(genOutputPath, configInputPath, genFilePath);
		} else if("4".equals(runType)) {
			if(args.length < 4) {
				System.out.println("缺少运行参数" + Arrays.toString(args));
				return;
			}
			String genOutputPath = args[1];
			String configInputPath = args[2];
			String genFilePath = args[3];
			X1CsharpDesignGenerator.genCode(genOutputPath, configInputPath, genFilePath);
		} else if("5".equals(runType)) {
			if(args.length < 4) {
				System.out.println("缺少运行参数" + Arrays.toString(args));
				return;
			}
			String genOutputPath = args[1];
			String configInputPath = args[2];
			String genFilePath = args[3];
			X1JavaConfigGenerator.genCode(genOutputPath, configInputPath, genFilePath);
		}
	}
	
	private static void export(Collection<DesignFile> fileMetas, String outputPath, String outputType, ExportType exportType) {
		if("csv".equals(outputType)) {
			CsvExporter.export(fileMetas, outputPath, exportType);
		} else if("json".equals(outputType)) {
			JsonExporter.export(fileMetas, outputPath, exportType);
		} else {
			System.out.println("无法处理的导出类型" + outputType);
		}
	}
	
	public static void test() {
		String inputPath = "";
		String docOutputPath = "";
		String docOutputType = "";
		String serverOutputPath = "";
		String serverOutputType = "";
		String clientOutputPath = "";
		String clientOutputType = "";
		String langFileExcelName = "";
		String langFileClassName = "";
		
		inputPath = "E:\\Workspace\\Svn\\x1\\doc\\config\\excel";
		docOutputPath = "E:\\Workspace\\Svn\\x1\\doc\\config\\export\\doc";
		docOutputType = "csv";
		serverOutputPath = "E:\\Workspace\\Svn\\x1\\doc\\config\\export\\server";
		serverOutputType = "csv";
		clientOutputPath = "E:\\Workspace\\Svn\\x1\\doc\\config\\export\\client";
		clientOutputType = "json";
		langFileExcelName = "Y语言表自动生成.xlsx";
		langFileClassName = "LanguageAuto";
		
		//1、根据配置表导出语言表内容
		LanguageFileGenerator.genLangFile(inputPath, langFileExcelName, langFileClassName);
		//2、导出配置表数据
		DesignExporter.export(inputPath, docOutputPath, docOutputType, ExportType.BOTH);
		DesignExporter.export(inputPath, serverOutputPath, serverOutputType, ExportType.S);
		DesignExporter.export(inputPath, clientOutputPath, clientOutputType, ExportType.C);
	}
	
}
