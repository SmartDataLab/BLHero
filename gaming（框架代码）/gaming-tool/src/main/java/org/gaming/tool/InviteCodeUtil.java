/**
 * 
 */
package org.gaming.tool;

/**
 * @author YY
 *
 */
public class InviteCodeUtil {
	
	public final static String[] CODE_LETTERS = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
			"G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	
	/**
	 * ID转换为邀请码
	 * @param id
	 * @param codeLetters
	 * @return
	 */
	public static String idToCode(long id, String[] codeLetters) {
		String code = "";
		while(id > 0) {
			int bit = (int)(id % codeLetters.length);
			code = codeLetters[bit] + code;
			id = id / codeLetters.length;
		}
		return code;
	}
	
	/**
	 * 邀请码转换为ID
	 * @param code
	 * @param codeLetters
	 * @return
	 */
	public static long codeToId(String code, String[] codeLetters) {
		String[] parts = code.split("");
		long id = 0;
		for(String part : parts) {
			int bit = 0;
			for(int i = 0; i < codeLetters.length; i++) {
				String letter = codeLetters[i];
				if(letter.equals(part)) {
					bit = i;
					break;
				}
			}
			id = id * codeLetters.length + bit;
		}
		return id;
	}
	
	public static void main(String[] args) {
		System.out.println(idToCode(484513, CODE_LETTERS));
		System.out.println(codeToId("GT73", CODE_LETTERS));
	}
}
