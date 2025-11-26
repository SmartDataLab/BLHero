package org.gaming.db;


/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		//异或加解密
		//原字符
        String oldstring = "卡696z了手颙pesorp机号登sa1d36s群无录卡视角的";

        System.out.println(oldstring);
        String newString = xor(oldstring, 'a');
        System.out.println(newString);
        System.out.println(xor(newString, 'a'));
		
	}
	
	public static String xor(String src, char key) {
		char[] cs = src.toCharArray();
		for(int i = 0; i < cs.length; i++) {
			cs[i] = (char)(cs[i] ^ key);
		}
		return new String(cs);
	}
}
