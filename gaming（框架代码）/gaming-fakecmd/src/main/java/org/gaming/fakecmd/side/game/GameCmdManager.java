/**
 * 
 */
package org.gaming.fakecmd.side.game;

import org.gaming.fakecmd.side.common.InternalCmdRegister;

/**
 * @author YY
 *
 */
public class GameCmdManager {

	public static void registerBean(Object bean) {
		PlayerCmdRegister.INS.register(bean);
		InternalCmdRegister.INS.register(bean);
		PlayerGmCmdRegister.INS.register(bean);
		CrossCmdRegister.INS.register(bean);
		PlayerCrossCmdRegister.INS.register(bean);
		PlayerToOtherRequestCmdRegister.INS.register(bean);
		PlayerToOtherResponseCmdRegister.INS.register(bean);
	}
	
	public static void printCmd() {
		System.out.println("===========ProtocolCmd===========");
		PlayerCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========InternalCmd===========");
		InternalCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========GmCmd===========");
		PlayerGmCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========CrossCmd===========");
		CrossCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========PlayerCrossCmd===========");
		PlayerCrossCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========PlayerCrossJumpCmdRequestRegister===========");
		PlayerToOtherRequestCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========PlayerCrossJumpResponseCmdRegister===========");
		PlayerToOtherResponseCmdRegister.INS.printAllInvokers();
	}
}
