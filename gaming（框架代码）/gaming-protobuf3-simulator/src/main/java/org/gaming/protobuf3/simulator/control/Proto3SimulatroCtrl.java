/**
 * 
 */
package org.gaming.protobuf3.simulator.control;

import org.gaming.protobuf3.simulator.protocol.ProtocolBuilder;
import org.gaming.protobuf3.simulator.protocol.ProtocolCenter;
import org.gaming.protobuf3.simulator.protocol.ProtocolMessage;
import org.gaming.protobuf3.simulator.protocol.ProtocolPrinter;
import org.gaming.simulator.netty.Netty;
import org.gaming.simulator.ui.slim.ISimulatorCtrl;
import org.gaming.simulator.ui.slim.SimulatorView;
import org.gaming.simulator.ui.slim.panel.LinkType;
import org.gaming.simulator.ui.slim.panel.ProtocolSelectPanel;

import com.google.protobuf.AbstractMessage;

/**
 * @author YY
 *
 */
public class Proto3SimulatroCtrl implements ISimulatorCtrl {

	@Override
	public void onDisconnect(SimulatorView view) {
		if(Netty.ins().getChannel() != null && Netty.ins().getChannel().isActive()) {
			Netty.ins().getChannel().disconnect();
		}
	}

	@Override
	public SendResult onConnect(SimulatorView view) {
		if(Netty.ins().getChannel() != null && Netty.ins().getChannel().isActive()) {
			Netty.ins().getChannel().disconnect();
		}
		
		String openId = view.getControlWestPanel().getConnectPanel().getOpenIdField().getText();
		
		String hostIp = view.getControlWestPanel().getConnectPanel().getHostField().getText();
		
		int areaId = Integer.parseInt(view.getControlWestPanel().getConnectPanel().getServerIdField().getText());
		
		LinkType linkType = view.getControlWestPanel().getConnectPanel().getLinkType();
		Netty.ins().connect(hostIp, linkType);
		
		AbstractMessage pbMessage = ProtocolCenter.pluginLogic.login(openId, areaId);
		return sendMessage(pbMessage);
	}

	@Override
	public SendResult onSend(ProtocolSelectPanel selectPanel) {
		String protocolName = selectPanel.getProtocolComboxPanel().getProtocolCombox().getSelectedProtocolName();
		
		Class<? extends AbstractMessage> selectClz = ProtocolCenter.getRequestProtocol(protocolName);
		Object builded = ProtocolBuilder.build(selectClz, selectPanel.getProtocolText());

		return sendMessage((AbstractMessage) builded);
	}
	
	private SendResult sendMessage(AbstractMessage builded) {
		ProtocolMessage message = ProtocolCenter.pluginLogic.encode(builded.getClass().getSimpleName(), builded);
		Netty.ins().getChannel().writeAndFlush(message.getContent());
		
		SendResult sendResult = new SendResult();
		sendResult.setTitle("================消息ID：" + message.getMessageId() + " 协议号：" + message.getProtocolId() + " 协议类：" + builded.getClass().getSimpleName() + " 发送时间：" + System.currentTimeMillis() + "================");
		sendResult.setContent(ProtocolPrinter.print(builded));
		return sendResult;
	}
}
