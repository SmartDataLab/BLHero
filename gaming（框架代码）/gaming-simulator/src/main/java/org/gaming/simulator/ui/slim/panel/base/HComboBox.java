/**
 * 
 */
package org.gaming.simulator.ui.slim.panel.base;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * @author YY
 *
 */
public class HComboBox<T> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JComboBox<T> comboBox;
	
	public HComboBox() {
		this(100, 25);
	}
	
	public HComboBox(int width, int height) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		comboBox = new JComboBox<>();
		comboBox.setPreferredSize(new Dimension(width, height));
		comboBox.setMaximumRowCount(32);
		this.add(comboBox, gbc);
	}
	
	public void setEditable(boolean editable) {
		comboBox.setEditable(editable);
	}
	
	public Object getSelectedItem() {
		return comboBox.getSelectedItem();
	}
	
	public void addItem(T item) {
		comboBox.addItem(item);
	}
	
	public void setSelectedIndex(int index) {
		comboBox.setSelectedIndex(index);
	}
	public void addListener(ItemListener listener) {
		comboBox.addItemListener(listener);
	}
}
