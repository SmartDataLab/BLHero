/**
 * 
 */
package org.gaming.simulator.ui.slim.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import org.gaming.simulator.ui.component.ProtocolCombox;

/**
 * @author YY
 *
 */
public class ProtocolComboxPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProtocolCombox protocolCombox;
	
	public ProtocolComboxPanel() {
		this(200, 23);
	}
	
	public ProtocolComboxPanel(int width, int height) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		protocolCombox = new ProtocolCombox();
		protocolCombox.setMaximumRowCount(32);
		protocolCombox.setPreferredSize(new Dimension(width, height));
		this.add(protocolCombox, gbc);
	}

	public ProtocolCombox getProtocolCombox() {
		return protocolCombox;
	}
	
	
}
