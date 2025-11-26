/**
 * 
 */
package org.gaming.simulator.ui.slim;

import org.gaming.simulator.ui.slim.panel.ProtocolSelectPanel;

/**
 * @author YY
 *
 */
public interface ISimulatorCtrl {
	
	SendResult onConnect(SimulatorView view);
	
	void onDisconnect(SimulatorView view);
	
	SendResult onSend(ProtocolSelectPanel selectPanel);
	
	static class SendResult {
		private String title;
		private String content;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
	}
}
