/**
 * 
 */
package org.gaming.simulator.ui.slim.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.gaming.simulator.ui.component.IComboxSelectedListener;
import org.gaming.simulator.ui.slim.SimulatorView;
import org.gaming.simulator.ui.slim.panel.base.HVerticalPanel;

/**
 * @author YY
 *
 */
public class ControlWestPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConnectNorthPanel connectPanel;
	
	private ProtocolSelectPanel firstProtocolPanel;
	private ProtocolSelectPanel secondProtocolPanel;
	private ProtocolSelectPanel thirdProtocolPanel;
	
	public ControlWestPanel() {
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 4));
		this.setPreferredSize(new Dimension((int)(SimulatorView.getWidth() * 0.255f), 0));
		this.setLayout(new BorderLayout());
		
		connectPanel = new ConnectNorthPanel();
		firstProtocolPanel = new ProtocolSelectPanel(0.20f);
		secondProtocolPanel = new ProtocolSelectPanel(0.264f);
		thirdProtocolPanel = new ProtocolSelectPanel(0.301f);
		
		this.add(connectPanel, BorderLayout.NORTH);
		
		HVerticalPanel verticalPanel = new HVerticalPanel();
		verticalPanel.append(firstProtocolPanel);
		verticalPanel.append(secondProtocolPanel);
		verticalPanel.append(thirdProtocolPanel);
		this.add(verticalPanel, BorderLayout.CENTER);
	}

	public ConnectNorthPanel getConnectPanel() {
		return connectPanel;
	}
	
	public void initProtocolPanel(Class<? extends IComboxSelectedListener> clazz, Map<String, Map<Integer, String>> groupedMap) {
		ProtocolSelectPanel[] selectPanels = new ProtocolSelectPanel[] {firstProtocolPanel, secondProtocolPanel, thirdProtocolPanel};
		for(ProtocolSelectPanel selectPanel : selectPanels) {
			try {
				IComboxSelectedListener listener = clazz.newInstance();
				listener.setTextArea(selectPanel.getProtocolArea());
				selectPanel.getProtocolComboxPanel().getProtocolCombox().initWithGroup(listener, groupedMap);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ProtocolSelectPanel[] getSelectPanels() {
		return new ProtocolSelectPanel[] {firstProtocolPanel, secondProtocolPanel, thirdProtocolPanel};
	}
}
