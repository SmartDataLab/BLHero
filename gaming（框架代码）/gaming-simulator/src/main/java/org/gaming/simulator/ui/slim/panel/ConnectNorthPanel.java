/**
 * 
 */
package org.gaming.simulator.ui.slim.panel;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gaming.simulator.ui.script.SilmUselogReader;
import org.gaming.simulator.ui.script.SilmUselogReader.SilmUselog;
import org.gaming.simulator.ui.slim.panel.base.HComboBox;
import org.gaming.simulator.ui.slim.panel.base.HHorizonPanel;
import org.gaming.simulator.ui.slim.panel.base.HTextField;
import org.gaming.simulator.ui.slim.panel.base.Label;

/**
 * @author YY
 *
 */
public class ConnectNorthPanel extends JPanel {

	public static LinkType DEFAULT_LINK_TYPE = LinkType.Tcp;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final HComboBox<String> useLogField;
	private final HTextField serverIdField;
	private final HTextField openIdField;
	private final HTextField hostField;
	
	private final JButton disconnectBtn;
	private final JButton connectBtn;
	
	private final JRadioButton tcpRadio;
	private final JRadioButton wsRadio;
	private final JRadioButton wssRadio;
	
	private final JCheckBox expandBox;
	
	public ConnectNorthPanel() {
		this.setLayout(new GridLayout(5, 1, 0, 0));
		
		HHorizonPanel linkTypePanel = new HHorizonPanel();
		linkTypePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		if(LinkType.Tcp == DEFAULT_LINK_TYPE) {
			tcpRadio = new JRadioButton("tcp", true);
			wsRadio = new JRadioButton("ws");
			wssRadio = new JRadioButton("wss");
		} else if(LinkType.Ws == DEFAULT_LINK_TYPE) {
			tcpRadio = new JRadioButton("tcp");
			wsRadio = new JRadioButton("ws", true);
			wssRadio = new JRadioButton("wss");
		} else if(LinkType.Wss == DEFAULT_LINK_TYPE) {
			tcpRadio = new JRadioButton("tcp");
			wsRadio = new JRadioButton("ws");
			wssRadio = new JRadioButton("wss", true);
		} else {
			tcpRadio = new JRadioButton("tcp", true);
			wsRadio = new JRadioButton("ws");
			wssRadio = new JRadioButton("wss");
		}
		
		ButtonGroup group = new ButtonGroup();
		group.add(tcpRadio);
		group.add(wsRadio);
		group.add(wssRadio);
		linkTypePanel.append(tcpRadio);
		linkTypePanel.append(wsRadio);
		linkTypePanel.append(wssRadio);
		
		expandBox = new JCheckBox();
		expandBox.setText("默认展开");
		expandBox.setSelected(true);
		linkTypePanel.append(expandBox);
		
		HHorizonPanel useLogPanel = new HHorizonPanel();
		useLogPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		useLogPanel.append(Label.create("Use Log", 60, 25));
		useLogField = new HComboBox<>(256, 23);
		useLogField.setEditable(true);
		useLogPanel.append(useLogField);
		
		HHorizonPanel serverIdPanel = new HHorizonPanel();
		serverIdPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		serverIdPanel.append(Label.create("Server ID", 60, 25));
		serverIdPanel.append(serverIdField = new HTextField(200, 23));
		
		HHorizonPanel openIdPanel = new HHorizonPanel();
		openIdPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		openIdPanel.append(Label.create("Open ID", 60, 25));
		openIdPanel.append(openIdField = new HTextField(200, 23));
		openIdPanel.append(disconnectBtn = new JButton("断开"));
		
		HHorizonPanel hostPanel = new HHorizonPanel();
		hostPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		hostPanel.append(Label.create("Host", 60, 25));
		hostPanel.append(hostField = new HTextField(200, 23));
		hostPanel.append(connectBtn = new JButton("连接"));
		
		this.add(linkTypePanel);
		this.add(useLogPanel);
		this.add(serverIdPanel);
		this.add(openIdPanel);
		this.add(hostPanel);
		
		useLogField.addListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					SilmUselog silmUselog = SilmUselogReader.parse(e.getItem().toString());
					serverIdField.setText(silmUselog.getServerId());
					openIdField.setText(silmUselog.getOpenId());
					hostField.setText(silmUselog.getHost());
				}
			}
		});
	}


	public HTextField getServerIdField() {
		return serverIdField;
	}

	public HTextField getOpenIdField() {
		return openIdField;
	}

	public HTextField getHostField() {
		return hostField;
	}

	public JButton getDisconnectBtn() {
		return disconnectBtn;
	}

	public JButton getConnectBtn() {
		return connectBtn;
	}
	
	public LinkType getLinkType() {
		if(tcpRadio.isSelected()) {
			return LinkType.Tcp;
		} else if(wsRadio.isSelected()) {
			return LinkType.Ws;
		} else if(wssRadio.isSelected()) {
			return LinkType.Wss;
		} else {
			return LinkType.Tcp;
		}
	}
	
	public boolean isDefauleExpand() {
		return expandBox.isSelected();
	}


	public HComboBox<String> getUseLogField() {
		return useLogField;
	}
}
