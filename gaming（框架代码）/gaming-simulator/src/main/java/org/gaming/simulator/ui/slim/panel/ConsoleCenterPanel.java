/**
 * 
 */
package org.gaming.simulator.ui.slim.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.gaming.simulator.ui.component.PopupMenu;
import org.gaming.simulator.ui.slim.SimulatorView;
import org.gaming.simulator.ui.slim.panel.base.HVerticalPanel;

/**
 * @author YY
 *
 */
public class ConsoleCenterPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HVerticalPanel panel;
	private JScrollPane scrollPane;
	
	private AdjustmentListener adjustmentListener;
	
	public ConsoleCenterPanel() {
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout());
		
		panel = new HVerticalPanel();
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(panel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		this.add(scrollPane, BorderLayout.CENTER);
		
		adjustmentListener = new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				e.getAdjustable().removeAdjustmentListener(adjustmentListener);
			}
		};
	}
	
	public void appendText(String btnText, String consoleText) {
		boolean defauleExpand = SimulatorView.instance().isDefaultExpand();
		
		JButton button = new JButton();
		if(defauleExpand) {
			button.setText("︾" + btnText);
		} else {
			button.setText(">>" + btnText);
		}
		button.setHorizontalAlignment(SwingConstants.LEFT);
		panel.append(button);
		
		JTextArea textArea = new JTextArea();
		if(defauleExpand) {
			textArea.setPreferredSize(null);
		} else {
			textArea.setPreferredSize(new Dimension(panel.getWidth(), 0));
		}
		panel.append(textArea);
		textArea.setText(consoleText);
		
		PopupMenu.installToTextComponent(textArea);
		
		button.addActionListener(new ActionListener() {
			private boolean expand = SimulatorView.instance().isDefaultExpand();
			@Override
			public void actionPerformed(ActionEvent e) {
				expand = !expand;
				if(expand) {
					button.setText("︾" + btnText);
					textArea.setPreferredSize(null);
					textArea.setText(consoleText);
				} else {
					button.setText(">>" + btnText);
					textArea.setPreferredSize(new Dimension(panel.getWidth(), 0));
					textArea.setText(consoleText);
				}
				panel.repaint();
			}
		});
		
		scrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
		panel.repaint();
	}
	
	public void cleanAllMessage() {
		panel.removeAll();
		panel.repaint();
	}
}
