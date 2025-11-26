/**
 * 
 */
package org.gaming.simulator.ui.slim.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gaming.simulator.ui.slim.SimulatorView;
import org.gaming.simulator.ui.slim.panel.base.HButton;
import org.gaming.simulator.ui.slim.panel.base.HHorizonPanel;
import org.gaming.simulator.ui.slim.panel.base.HTextField;

/**
 * @author YY
 *
 */
public class ProtocolSelectPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private ProtocolComboxPanel protocolComboxPanel;
	
	private HButton sendBtn;
	
	private JTextArea protocolArea;
	
	private HTextField searchText;
	
	public ProtocolSelectPanel(float heightRate) {
		this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		this.setPreferredSize(new Dimension(0, (int)(SimulatorView.getHeight() * heightRate)));
		
		this.setLayout(new BorderLayout());
		
		protocolComboxPanel = new ProtocolComboxPanel();
		
		HHorizonPanel northPanel = new HHorizonPanel();
		northPanel.append(searchText = new HTextField(60, 23));
		northPanel.append(protocolComboxPanel);
		northPanel.append(sendBtn = new HButton("发送", 57, 25));
		this.add(northPanel, BorderLayout.NORTH);
		
		
		protocolArea = new JTextArea();
		JScrollPane protocolPanel = new JScrollPane();
		protocolPanel.setViewportView(protocolArea);
		JPanel protocolOuterPanel = new JPanel();
		protocolOuterPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		protocolOuterPanel.setLayout(new BorderLayout());
		protocolOuterPanel.add(protocolPanel, BorderLayout.CENTER);
		
		this.add(protocolOuterPanel, BorderLayout.CENTER);
	}
	
	public void setProtocolText(String text) {
		protocolArea.setText(text);
	}
	
	public String getProtocolText() {
		return protocolArea.getText();
	}
	
	public void addButtonListener(ActionListener actionListener) {
		sendBtn.addActionListener(actionListener);
	}
	
	public void addSearchListener(KeyListener keyListener) {
		searchText.getTextField().addKeyListener(keyListener);
	}

	public ProtocolComboxPanel getProtocolComboxPanel() {
		return protocolComboxPanel;
	}

	public JTextArea getProtocolArea() {
		return protocolArea;
	}
	
	public void setSearchText(String search) {
		searchText.getTextField().setText(search);
		protocolComboxPanel.getProtocolCombox().filterSelection(search);
	}
	
	public void filterSelection(String searchText) {
		protocolComboxPanel.getProtocolCombox().filterSelection(searchText);
	}
}
