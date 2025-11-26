/**
 * 
 */
package org.gaming.simulator.ui.slim.panel.base;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author YY
 *
 */
public class HTextField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField textField;
	
	public HTextField() {
		this(100, 25);
	}
	
	public HTextField(int width, int height) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(width, height));
		this.add(textField, gbc);
	}
	
	public void setText(Object text) {
		textField.setText(text.toString());
	}
	
	public String getText() {
		return textField.getText();
	}

	public JTextField getTextField() {
		return textField;
	}
}
