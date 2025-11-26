package org.gaming.fakecmd.side.cross;

import org.gaming.fakecmd.side.common.InternalCmdRegister;

/**
 * 
 * @author YY
 *
 */
public class CrossCmdManager {

	public static void registerBean(Object bean) {
		InternalCmdRegister.INS.register(bean);
		CrossCmdRegister.INS.register(bean);
		PlayerCrossCmdRegister.INS.register(bean);
	}
	
	public static void printCmd() {
		System.out.println("===========InternalCmd===========");
		InternalCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========CrossCmd===========");
		CrossCmdRegister.INS.printAllInvokers();
		
		System.out.println("===========PlayerCrossCmd===========");
		PlayerCrossCmdRegister.INS.printAllInvokers();
	}
}
