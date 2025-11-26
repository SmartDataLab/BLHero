/**
 * 
 */
package org.gaming.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author YY
 *
 */
public class FileUtil {

	/**
	 * 列举某个文件夹下所有文件的名称
	 * @param fileDir
	 * @return
	 */
	public static Set<String> listFileNames(String fileDir) {
		Set<String> fileNames = new HashSet<>();
		File file = new File(fileDir);
		listFileNames(file, fileNames);
		return fileNames;
	}

	private static void listFileNames(File file, Set<String> fileNames) {
		if (file.isDirectory()) {
			for (File tempFile : file.listFiles()) {
				listFileNames(tempFile, fileNames);
			}
		} else {
			fileNames.add(file.getName());
		}
	}
	
	/**
	 * 列举某个文件夹下所有文件的完整路径
	 * @param fileDir
	 * @return
	 */
	public static Set<String> listFilePaths(String fileDir) {
		Set<String> fileNames = new HashSet<>();
		File file = new File(fileDir);
		listFilePaths(file, fileNames);
		return fileNames;
	}
	
	private static void listFilePaths(File file, Set<String> fileNames) {
		if (file.isDirectory()) {
			for (File tempFile : file.listFiles()) {
				listFilePaths(tempFile, fileNames);
			}
		} else {
			fileNames.add(file.getPath());
		}
	}
	
	/**
	 * 列举某个文件夹下所有的文件
	 * @param file
	 * @return
	 */
	public static List<File> listFiles(String fileDir) {
		List<File> resultFiles = new ArrayList<>();
		File file = new File(fileDir);
		listFiles(file, resultFiles);
		return resultFiles;
	}
	
	private static void listFiles(File file, List<File> resultFiles) {
		if(file.isDirectory()) {
			for(File subFile : file.listFiles()) {
				listFiles(subFile, resultFiles);
			}
		} else {
			resultFiles.add(file);
		}
	}

	/**
	 * 写文件
	 * 
	 * @param fileDir
	 * @param fileName
	 * @param content
	 */
	public static void writeToFile(String fileDir, String fileName, String content) {
		try {
			File dirFile = new File(fileDir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			String filePath = fileDir + File.separator + fileName;
			File file = new File(filePath);
			OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
			os.append(content);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFile(String fileDir, String fileName, byte[] content) {
		File dirFile = new File(fileDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String filePath = fileDir + File.separator + fileName;
		File file = new File(filePath);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> readTxtLines(File file) {
		List<String> list = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				list.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String readTxtFile(File file) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				builder.append(line);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	public static byte[] readFile(String fileDir, String fileName) {
		File file = new File(fileDir + File.separator + fileName);
		if(!file.exists()) {
			return new byte[0];
		}
		return readFile(file);
	}
	
	public static byte[] readFile(File file) {
		if (file.isDirectory()) {
			return new byte[0];
		}
		BufferedInputStream bis = null;
		byte[] data = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int c = 0;
		byte[] buffer = new byte[8 * 1024];
		try {
			while ((c = bis.read(buffer)) != -1) {
				baos.write(buffer, 0, c);
				baos.flush();
			}
			data = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void zip(String zipFilePath, String sourceFilePath) {
		ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
            
            File sourceFile = new File(sourceFilePath);
            compress(zos, sourceFile, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(zos != null) {
        		try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}
	
	
	public static void zip(String zipFilePath, List<File> files) {
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
			for(File file : files) {
				compress(zos, file, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(zos != null) {
        		try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
	}
	
	/**
	 * 压缩文件
	 * @param zos
	 * @param sourceFile
	 * @param rootPath
	 * @throws Exception
	 */
	private static void compress(ZipOutputStream zos, File sourceFile, String rootPath) throws Exception {
		if (sourceFile.isFile()) {
			zos.putNextEntry(new ZipEntry(rootPath + File.separator + sourceFile.getName()));
			
			byte[] buf = new byte[1024];
			int len = 0;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			in.close();
		} else {
			rootPath = rootPath + File.separator + sourceFile.getName();
			
			File[] listFiles = sourceFile.listFiles();
			zos.putNextEntry(new ZipEntry(rootPath + File.separator));
			zos.closeEntry();
			
			for (File file : listFiles) {
				compress(zos, file, rootPath);
			}
		}
	}
	
	/**
	 * 文件的MD5
	 * @param file
	 * @return
	 */
	public static String fileMD5(File file) {
		String fileContext = readTxtFile(file);
		return MD5Util.getMD5(fileContext);
	}
}
