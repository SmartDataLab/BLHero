/**
 * 
 */
package org.gaming.simulator.ui.slim;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.gaming.simulator.ui.base.ScreenConfig;
import org.gaming.simulator.ui.slim.panel.ConsoleCenterPanel;
import org.gaming.simulator.ui.slim.panel.ControlWestPanel;

/**
 * @author YY
 *
 */
public class SimulatorView {

	private static SimulatorView simulator = new SimulatorView();
	
	private SimulatorView() {
		initialize();
	}
	public static SimulatorView instance() {
		return simulator;
	}
	
	private JFrame frame;
	//框体的标题
	private String title = "";
	
	private ControlWestPanel controlWestPanel;
	private ConsoleCenterPanel consoleCenterPanel;
	
	public JFrame getFrame() {
		return frame;
	}
	
	private void initialize() {
		//这些是Swing的一些外观主题，替换到setLookAndFeel可以看到效果
		//javax.swing.plaf.metal.MetalLookAndFeel
		//javax.swing.plaf.nimbus.NimbusLookAndFeel
		//com.sun.java.swing.plaf.motif.MotifLookAndFeel
		//com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		//com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		//之后将会把画板添加到窗框中
		frame = new JFrame();
		//窗口的宽高
		int width = 1275;
		int height = 845;
		
		int x = (ScreenConfig.SCREEN_WIDTH - width) / 2;
		int y = (ScreenConfig.SCREEN_HEIGHT - height) / 2;
		
		frame.setTitle(title);
		frame.setBounds(x, y, width, height);
		//设置右上角关闭按钮点击后也终止程序进程
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static int getWidth() {
		return SimulatorView.instance().getFrame().getWidth();
	}
	public static int getHeight() {
		return SimulatorView.instance().getFrame().getHeight();
	}
	
	
	public void layout() {
		this.frame.setLayout(new BorderLayout());
		
		this.frame.add(controlWestPanel = new ControlWestPanel(), BorderLayout.WEST);
		this.frame.add(consoleCenterPanel = new ConsoleCenterPanel(), BorderLayout.CENTER);
		
	}
	
	public static void start() {
		SimulatorView.instance().layout();
		SimulatorView.instance().getFrame().setVisible(true);
	}
	
	public static void main(String[] args) {
		SimulatorView.start();
	}
	public ControlWestPanel getControlWestPanel() {
		return controlWestPanel;
	}
	public ConsoleCenterPanel getConsoleCenterPanel() {
		return consoleCenterPanel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		frame.setTitle(title);
		this.title = title;
	}
	public boolean isDefaultExpand() {
		return this.controlWestPanel.getConnectPanel().isDefauleExpand();
	}
}
