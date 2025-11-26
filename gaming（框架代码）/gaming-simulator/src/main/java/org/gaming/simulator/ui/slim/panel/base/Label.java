/**
 * 
 */
package org.gaming.simulator.ui.slim.panel.base;

import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * @author YY
 *
 */
public class Label {

	public static JLabel create(String text, int width, int height) {
		JLabel label = new JLabel(text);
		label.setPreferredSize(new Dimension(width, height));
		return label;
	}
}
