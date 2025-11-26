/**
 * 
 */
package org.gaming.simulator.ui.slim.panel.base;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author YY
 *
 */
public class HVerticalPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel lastPanel;
	
	public HVerticalPanel() {
		this.setLayout(new BorderLayout());
	}
	
	
	public void append(JComponent component) {
		if(lastPanel == null) {
			lastPanel = this;
		}
		
		lastPanel.add(component, BorderLayout.NORTH);
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());
		lastPanel.add(jPanel, BorderLayout.CENTER);
		
		lastPanel = jPanel;
	}


	@Override
	public void removeAll() {
		super.removeAll();
		lastPanel = null;
	}
}
