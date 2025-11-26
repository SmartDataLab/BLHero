/**
 * 
 */
package org.gaming.protobuf3.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.gaming.protobuf3.simulator.control.Proto3ComboxCtrl;
import org.gaming.protobuf3.simulator.control.Proto3SimulatroCtrl;
import org.gaming.protobuf3.simulator.plugin.PluginLogic;
import org.gaming.protobuf3.simulator.protocol.ProtocolCenter;
import org.gaming.simulator.netty.Netty;
import org.gaming.simulator.ui.base.ViewManager;
import org.gaming.simulator.ui.slim.SimulatorCtrl;
import org.gaming.simulator.ui.slim.SimulatorView;
import org.gaming.simulator.ui.slim.panel.LinkType;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author YY
 *
 */
public class SimulatorMain {
	
	public static void start(String name, PluginLogic logic, Map<LinkType, ChannelInitializer<Channel>> channelInitializerMap, String fullUseLogFile) {
		//初始化Netty的编解码与连接信息
		Netty.init(channelInitializerMap);
		
		SimulatorView window = SimulatorView.instance();
		window.setTitle(name);
		window.layout();
		window.getFrame().setVisible(true);
		
		ProtocolCenter.setPluginLogic(logic);
		
		List<Integer> list = new ArrayList<>(ProtocolCenter.getIdRequestProtocols().keySet());
		Collections.sort(list);
		
		Map<String, Map<Integer, String>> groupedMap = new LinkedHashMap<>();
		for(Integer protocolId : list) {
			Class<?> clazz = ProtocolCenter.getRequestProtocol(protocolId);
			String[] names = clazz.getPackage().getName().split("\\.");
			
			String packageName = names[names.length - 1];
			Map<Integer, String> map = groupedMap.get(packageName);
			if(map == null) {
				map = new HashMap<>();
				groupedMap.put(packageName, map);
			}
			map.put(protocolId, clazz.getSimpleName());
		}
		
		window.getControlWestPanel().initProtocolPanel(Proto3ComboxCtrl.class, groupedMap);

		SimulatorCtrl ctrl = new SimulatorCtrl(window, new Proto3SimulatroCtrl());
		ctrl.initialize(fullUseLogFile);
		ViewManager.setIViewCtrl(ctrl);
	}
}
