/**
 * 
 */
package org.gaming.simulator.ui.slim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JTextField;

import org.gaming.simulator.ui.base.IViewCtrl;
import org.gaming.simulator.ui.script.SilmUselogReader;
import org.gaming.simulator.ui.script.SilmUselogReader.SilmUselog;
import org.gaming.simulator.ui.slim.ISimulatorCtrl.SendResult;
import org.gaming.simulator.ui.slim.panel.ConnectNorthPanel;
import org.gaming.simulator.ui.slim.panel.ProtocolSelectPanel;
import org.gaming.simulator.ui.slim.panel.base.HComboBox;

/**
 * @author YY
 *
 */
public class SimulatorCtrl implements IViewCtrl {

	private SimulatorView view;
	private ISimulatorCtrl ctrl;
	
	public SimulatorCtrl(SimulatorView view, ISimulatorCtrl ctrl) {
		this.view = view;
		this.ctrl = ctrl;
	}
	
	public void initialize(String fullUseLogFile) {
		List<SilmUselog> useLogs = SilmUselogReader.readUselog(fullUseLogFile);
		SilmUselog silmUselog = null;
		if(useLogs.isEmpty()) {
			silmUselog = SilmUselogReader.saveUselog("7", "1111", "127.0.0.1:9902");
		} else {
			silmUselog = useLogs.get(0);
		}
		
		final ConnectNorthPanel connectPanel = view.getControlWestPanel().getConnectPanel();
		
		connectPanel.getServerIdField().setText(silmUselog.getServerId());
		connectPanel.getOpenIdField().setText(silmUselog.getOpenId());
		connectPanel.getHostField().setText(silmUselog.getHost());
		
		for(SilmUselog log : useLogs) {
			connectPanel.getUseLogField().addItem(log.resume());
		}
		if(!useLogs.isEmpty()) {
			connectPanel.getUseLogField().setSelectedIndex(0);
		}
		//断开按钮绑定事件
		connectPanel.getDisconnectBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.onDisconnect(view);
			}
		});
		//连接按钮绑定事件
		connectPanel.getConnectBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					addResult(ctrl.onConnect(view));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				String serverId = connectPanel.getServerIdField().getText();
				String openId = connectPanel.getOpenIdField().getText();
				String host = connectPanel.getHostField().getText();
				
				System.out.println(serverId + " " + openId + " " + host);
				
				HComboBox<String> useLogField = connectPanel.getUseLogField();
				SilmUselog log = SilmUselogReader.saveUselog(serverId, openId, host);
				if(log != null) {
					useLogField.addItem(log.resume());
					useLogField.repaint();
				}
			}
		});
		
		ProtocolSelectPanel[] selectPanels = view.getControlWestPanel().getSelectPanels();
		for(int i = 0; i < selectPanels.length; i++) {
			final int index = i;
			ProtocolSelectPanel selectPanel = selectPanels[i];
			if(i < silmUselog.getSearch().length) {
				String searchText = silmUselog.getSearch()[i];
				if(searchText != null) {
					selectPanel.setSearchText(searchText);
				}
			}
			//发送按钮绑定事件
			selectPanel.addButtonListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						addResult(ctrl.onSend(selectPanel));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			selectPanel.addSearchListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {

				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						JTextField textField = (JTextField)e.getSource();
						String searchContent = textField.getText();
						selectPanel.filterSelection(searchContent);
						//TODO 添加记录
						
						String serverId = connectPanel.getServerIdField().getText();
						String openId = connectPanel.getOpenIdField().getText();
						String host = connectPanel.getHostField().getText();
						SilmUselogReader.saveSearchText(serverId, openId, host, index, searchContent);
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {

				}
			});
		}
	}
	
	@Override
	public void addResult(SendResult sendResult) {
		view.getConsoleCenterPanel().appendText(sendResult.getTitle(), sendResult.getContent());
	}

	@Override
	public void clearResult() {
		view.getConsoleCenterPanel().cleanAllMessage();
	}
}
