/**
 * 
 */
package org.gaming.simulator.ui.script;

import java.util.List;
import java.util.UUID;

/**
 * @author YY
 *
 */
public class AccountReader {
	
	private static String path = AccountReader.class.getResource("/").getPath() + "account";
	
	public static void main(String[] args) {
		
		List<String> openIds = offerAccounts(50);
		
		for(String openId : openIds) {
			System.out.println(openId);
		}
//		
//		for(int i = 0; i < 50; i++) {
//			openIds.add(UUID.randomUUID().toString().substring(0, 8));
//		}
//		saveAccounts(openIds);
	}
	
	public static List<String> offerAccounts(int number) {
		List<String> accounts = ScriptReader.readScript(path);
		
		boolean needSave = false;
		for(int i = accounts.size(); i < number; i++) {
			accounts.add(UUID.randomUUID().toString().substring(0, 8));
			needSave = true;
		}
		if(needSave) {
			ScriptReader.saveScript(path, accounts);
		}
		return accounts.subList(0, number);
	}
}
