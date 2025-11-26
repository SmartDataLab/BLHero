/**
 * 
 */
package org.gaming.simulator.ui.component;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComboBox;

/**
 * @author YY
 *
 */
public class ProtocolCombox extends JComboBox<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isInited = false;
	
	private static String SPLITER = "-";
	
	private IComboxSelectedListener selectedListener;
	
	private Map<String, Map<Integer, String>> groupedProtocolMap;
	
	private ComboxListener comboxListener;
	
	public void init(IComboxSelectedListener selectedListener, Map<Integer, String> protocolMap) {
		this.selectedListener = selectedListener;
		if(isInited) {
			return;
		}
		isInited = true;
		List<Integer> list = new ArrayList<>(protocolMap.keySet());
		Collections.sort(list);
		
		for(Integer protocol : list) {
			String clzName = protocolMap.get(protocol);
			this.addItem(protocol + SPLITER + clzName);
		}
		this.addItemListener(new ComboxListener());
	}
	
	
	public void initWithGroup(IComboxSelectedListener selectedListener, Map<String, Map<Integer, String>> groupedProtocolMap) {
		this.selectedListener = selectedListener;
		if(isInited) {
			return;
		}
		isInited = true;
		this.groupedProtocolMap = groupedProtocolMap;
		
		for(Entry<String, Map<Integer, String>> groupEntry : groupedProtocolMap.entrySet()) {
			this.addItem("==========" + groupEntry.getKey() + "==========");
			
			List<Integer> list = new ArrayList<>(groupEntry.getValue().keySet());
			Collections.sort(list);
			for(Integer protocolId : list) {
				String clzName = groupEntry.getValue().get(protocolId);
				this.addItem(protocolId + SPLITER + clzName);
			}
		}
		comboxListener = new ComboxListener();
		this.addItemListener(comboxListener);
	}
	
	public void filterSelection(String filter) {
		this.removeAllItems();
		this.removeItemListener(comboxListener);
		
		filter = filter.toLowerCase();
		
		for(Entry<String, Map<Integer, String>> groupEntry : groupedProtocolMap.entrySet()) {
			boolean addTitle = false;
			for(Entry<Integer, String> entry : groupEntry.getValue().entrySet()) {
				if(entry.getKey().toString().toLowerCase().contains(filter) || entry.getValue().toLowerCase().contains(filter)) {
					addTitle = true;
				}
			}
			if(!addTitle) {
				continue;
			}
			this.addItem("==========" + groupEntry.getKey() + "==========");
			List<Integer> list = new ArrayList<>(groupEntry.getValue().keySet());
			Collections.sort(list);
			
			if(filter.equals(groupEntry.getKey())) {
				for(Integer protocolId : list) {
					String clzName = groupEntry.getValue().get(protocolId);
					this.addItem(protocolId + SPLITER + clzName);
				}
			} else {
				for(Integer protocolId : list) {
					String clzName = groupEntry.getValue().get(protocolId);
					if(protocolId.toString().toLowerCase().contains(filter) || clzName.toLowerCase().contains(filter)) {
						this.addItem(protocolId + SPLITER + clzName);
					}
				}
			}
		}
		this.addItemListener(comboxListener);
	}
	
	
	private class ComboxListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() != ItemEvent.SELECTED) {
				return;
			}
			String selectedProtocol = e.getItem().toString();
			String[] protocolStr = selectedProtocol.split(SPLITER);
			selectedListener.onSelect(Integer.valueOf(protocolStr[0]), protocolStr[1]);
		}
	}
	
	public String getSelectedProtocolName() {
		String[] protocolStr = this.getSelectedItem().toString().split(SPLITER);
		return protocolStr[1];
	}
}
