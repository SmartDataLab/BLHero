/**
 * 
 */
package org.gaming.simulator.ui;

import org.gaming.simulator.ui.slim.SimulatorView;

/**
 * @author YY
 *
 */
public class SimulatorMain {
	
//	public static void start(PluginLogic logic, Class<? extends ByteToMessageDecoder> decoderClazz,
//			Class<? extends MessageToByteEncoder<?>> encoderClass, String protoPackage, String protoClassPath) {
//		PluginLogic.logic = logic;
//		//初始化Netty的编解码与连接信息
//		Netty.init(decoderClazz, encoderClass);
//		//初始化协议的解释与注册
//		ProtocolCenter.init(protoPackage, protoClassPath);
//		
//		SimulatorView window = new SimulatorView();
//		window.getFrame().setVisible(true);
//		
//		Map<Integer, Class<? extends AbstractMessageLite>> protocolMap = ProtocolCenter.getRequestProtocols();
//		window.getProtocolCombox().listProtocol(protocolMap);
//		
//		SimulatorCtrl ctrl = new SimulatorCtrl(window);
//		ctrl.initialize();
//		ViewManager.setIViewCtrl(ctrl);
//	}
	
	
	public static void main(String[] args) {
		SimulatorView window = SimulatorView.instance();
		window.layout();
		window.getFrame().setVisible(true);
	}
}
