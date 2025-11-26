/**
 * 
 */
package com.xiugou.x1.simulator;

import org.gaming.protobuf3.simulator.protocol.ProtocolClassPrinter;
import org.gaming.protobuf3.simulator.protocol.ProtocolPrinter;

import pb.xiugou.x1.protobuf.hero.Hero.PbHero;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbPlayerExp;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbPlayerVipExp;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbThing;

/**
 * @author YY
 *
 */
public class PbThingPrinter implements ProtocolClassPrinter {

	@Override
	public void print(Object object, String offset, StringBuilder result, boolean isInArray) {
		PbThing pbThing = (PbThing)object;
		try {
			ProtocolPrinter.doPrint("identity:" + pbThing.getIdentity() + ",", offset, result, isInArray);
			ProtocolPrinter.doPrint("num:" + pbThing.getNum() + ",", offset, result, isInArray);
			
			if(pbThing.getIdentity() == 3) {
				PbPlayerExp pbPlayerExp = PbPlayerExp.parseFrom(pbThing.getData());
				ProtocolPrinter.doPrint(pbPlayerExp, offset, result, isInArray);
			} else if(1010000 < pbThing.getIdentity() && pbThing.getIdentity() < 1050000) {
				PbHero pbHero = PbHero.parseFrom(pbThing.getData());
				ProtocolPrinter.doPrint(pbHero, offset, result, isInArray);
			} else if(pbThing.getIdentity() == 7) {
				PbPlayerVipExp pbPlayerVipExp = PbPlayerVipExp.parseFrom(pbThing.getData());
				ProtocolPrinter.doPrint(pbPlayerVipExp, offset, result, isInArray);
			}
		} catch (Exception e) {
		}
	}

}
