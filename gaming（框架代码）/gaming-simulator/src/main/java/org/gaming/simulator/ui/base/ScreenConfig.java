/**
 * 
 */
package org.gaming.simulator.ui.base;

import java.awt.Toolkit;

/**
 * @author YY
 *
 */
public class ScreenConfig {

	/**
	 * 屏幕的宽
	 */
	public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth();
	/**
	 * 用户屏幕的高
	 */
	public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight();
}
