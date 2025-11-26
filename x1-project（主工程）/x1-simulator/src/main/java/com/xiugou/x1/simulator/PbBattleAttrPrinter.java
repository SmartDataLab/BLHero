/**
 * 
 */
package com.xiugou.x1.simulator;

import org.gaming.protobuf3.simulator.protocol.ProtocolClassPrinter;
import org.gaming.protobuf3.simulator.protocol.ProtocolPrinter;
import org.gaming.tool.GsonUtil;

import pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr;

/**
 * @author YY
 *
 */
public class PbBattleAttrPrinter implements ProtocolClassPrinter {

	@Override
	public void print(Object object, String offset, StringBuilder result, boolean isInArray) {
		PbBattleAttr pbBattleAttr = (PbBattleAttr)object;
		try {
			ProtocolPrinter.doPrint(GsonUtil.toJson(pbBattleAttr), offset, result, isInArray);
		} catch (Exception e) {
		}
	}
}
