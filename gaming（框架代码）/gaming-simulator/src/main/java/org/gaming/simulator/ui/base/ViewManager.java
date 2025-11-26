/**
 * 
 */
package org.gaming.simulator.ui.base;

import org.gaming.simulator.ui.slim.ISimulatorCtrl.SendResult;

/**
 * @author YY
 *
 */
public class ViewManager {
	
	private IViewCtrl iview;
	
	private static ViewManager manager = new ViewManager();
	private ViewManager(){}
	
	public static void addResult(SendResult sendResult) {
		if(ViewManager.manager.iview != null) {
			ViewManager.manager.iview.addResult(sendResult);
		}
	}
	
	public static void addResult(String title, String content) {
		SendResult sendResult = new SendResult();
		sendResult.setTitle(title);
		sendResult.setContent(content);
		addResult(sendResult);
	}
	
	public static void addResult(String title) {
		addResult(title, "");
	}
	
	public static void clearResult() {
		if(ViewManager.manager.iview != null) {
			ViewManager.manager.iview.clearResult();
		}
	}
	
	public static void setIViewCtrl(IViewCtrl iview) {
		ViewManager.manager.iview = iview;
	}
}
