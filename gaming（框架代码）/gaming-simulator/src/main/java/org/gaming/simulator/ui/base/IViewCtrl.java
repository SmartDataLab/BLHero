/**
 * 
 */
package org.gaming.simulator.ui.base;

import org.gaming.simulator.ui.slim.ISimulatorCtrl.SendResult;

/**
 * @author YY
 *
 */
public interface IViewCtrl {
	
	public void addResult(SendResult value);
	
	public void clearResult();
}
