/**
 * 
 */
package org.gaming.simulator.ui.slim.panel.base;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author YY
 *
 */
public class HButton extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton button;
	
	public HButton(String text) {
		this(text, 100, 25);
	}
	
	public HButton(String text, int width, int height) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		button = new JButton();
		button.setPreferredSize(new Dimension(width, height));
		button.setText(text);
		this.add(button, gbc);
	}
	
	public void setText(Object text) {
		button.setText(text.toString());
	}
	
	public String getText() {
		return button.getText();
	}
	
	public void addActionListener(ActionListener actionListener) {
		button.addActionListener(actionListener);
	}
}
