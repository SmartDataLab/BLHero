/**
 * 
 */
package org.gaming.simulator.ui.slim.panel.base;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author YY
 * 横向布局的面板
 */
public class HHorizonPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel lastPanel;
	
	public HHorizonPanel() {
		this.setLayout(new BorderLayout());
	}
	
	
	public void append(JComponent component) {
		if(lastPanel == null) {
			lastPanel = this;
		}
		
		lastPanel.add(component, BorderLayout.WEST);
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());
		lastPanel.add(jPanel, BorderLayout.CENTER);
		
		lastPanel = jPanel;
	}
	
}
