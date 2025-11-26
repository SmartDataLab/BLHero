/**
 * 
 */
package org.gaming.simulator.ui.component;

import javax.swing.JTextArea;

/**
 * @author YY
 *
 */
public interface IComboxSelectedListener {

	void setTextArea(JTextArea textArea);
	
	void onSelect(int protocolId, String protocolName);
}
