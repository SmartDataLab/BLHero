/**
 * 
 */
package org.gaming.simulator.ui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import org.gaming.simulator.ui.base.ViewManager;

/**
 * @author YY
 *
 */
public class PopupMenu extends JPopupMenu implements MouseListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenuItem clear;
	
	private static PopupMenu instance = new PopupMenu();
	private PopupMenu() {
		this.clear = new JMenuItem("清除");
		this.add(this.clear);
		
		this.clear.addActionListener(this);
	}
	public static PopupMenu getInstance() {
		return instance;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.clear) {
			ViewManager.clearResult();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger() && e.getSource() instanceof JTextComponent) {
			JTextComponent textComponent = (JTextComponent) e.getSource();
			textComponent.requestFocusInWindow();
			this.show(textComponent, e.getX(), e.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/** 
     * 为文本组件添加右键菜单 
     * @param textComponent 
     * @return 
     */
	public static boolean installToTextComponent(JTextComponent textComponent) {
		boolean isHasInstalled = false;
		MouseListener[] listeners = textComponent.getMouseListeners();

		if (null != listeners) {
			for (int i = 0; i < listeners.length; i++) {
				if (listeners[i] == PopupMenu.getInstance()) {
					isHasInstalled = true;
				}
			}
		}

		if (!isHasInstalled) {
			textComponent.addMouseListener(PopupMenu.getInstance());

			return true;
		}

		return false;
	}
	
}
