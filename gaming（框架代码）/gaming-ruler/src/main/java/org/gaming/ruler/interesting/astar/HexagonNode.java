/**
 * 
 */
package org.gaming.ruler.interesting.astar;

import java.util.List;

/**
 * @author YY
 * 正六边形
 */
public class HexagonNode<T> extends BaseAStarNode<T, HexagonNode<T>> {
	
	private int leftX;
	private int leftY;
	
	private int rightX;
	private int rightY;
	
	public HexagonNode(int id, int x, int y, int leftX, int leftY, int rightX, int rightY) {
		super(id, x, y);
		this.leftX = leftX;
		this.leftY = leftY;
		this.rightX = rightX;
		this.rightY = rightY;
	}
	
	@Override
	public String toString() {
		return "(" + leftX + ", " + leftY + "), (" + rightX + ", " + rightY + ")";
	}
	
	public boolean isSameAs(HexagonNode<T> node) {
		if (this.leftX == node.leftX && this.leftY == node.leftY && this.rightX == node.rightX
				&& this.rightY == node.rightY) {
			return true;
		}
		return false;
	}
	
	public double distanceWith(HexagonNode<T> node) {
		double dx1 = (this.leftX + this.rightX) / 2.0d;
		double dy1 = (this.leftY + this.rightY) / 2.0d;
		
		double dx2 = (node.leftX + node.rightX) / 2.0d;
		double dy2 = (node.leftY + node.rightY) / 2.0d;
		
		double dx = dx1 - dx2;
		double dy = dy1 - dy2;
		
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	@SuppressWarnings("unchecked")
	public void setAroundNodes(List<? extends HexagonNode<T>> aroundNodes) {
		this.aroundNodes = aroundNodes.toArray(new HexagonNode[aroundNodes.size()]);
	}

	public int getLeftX() {
		return leftX;
	}

	public int getLeftY() {
		return leftY;
	}

	public int getRightX() {
		return rightX;
	}

	public int getRightY() {
		return rightY;
	}
}
