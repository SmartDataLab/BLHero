/**
 * 
 */
package org.gaming.protobuf3.simulator.control;

import javax.swing.JTextArea;

import org.gaming.protobuf3.simulator.protocol.ProtocolCenter;
import org.gaming.protobuf3.simulator.protocol.ProtocolPrinter;
import org.gaming.simulator.ui.component.IComboxSelectedListener;

import com.google.protobuf.AbstractMessage;

/**
 * @author YY
 *
 */
public class Proto3ComboxCtrl implements IComboxSelectedListener {

	private JTextArea textArea;
	
	@Override
	public void onSelect(int protocolId, String protocolName) {
		Class<? extends AbstractMessage> selectClz = ProtocolCenter.getRequestProtocol(protocolName);
		String result = ProtocolPrinter.print(selectClz);
		textArea.setText(result);
	}

	@Override
	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}
}
