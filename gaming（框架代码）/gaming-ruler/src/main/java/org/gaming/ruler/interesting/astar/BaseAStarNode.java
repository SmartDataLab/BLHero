/**
 * 
 */
package org.gaming.ruler.interesting.astar;

/**
 * @author YY
 *
 */
public abstract class BaseAStarNode<E, Node extends BaseAStarNode<E, Node>> {
	/**
	 * 节点ID
	 */
	private final int id;
	private final int x;
	private final int y;
	
	private int g;//从起点移动到当前点的消耗
	private int h;//从当前点移动到目标点的预估消耗
	private int f;//f=g+h
	
	private int closeVersion;//是否在open表中
	private int openVersion;//是否在close表中
	
	private E entity;
	
	private Node father;//父亲
	protected Node[] aroundNodes;//节点周围的节点
	
	protected BaseAStarNode(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getCloseVersion() {
		return closeVersion;
	}

	public void setCloseVersion(int closeVersion) {
		this.closeVersion = closeVersion;
	}

	public int getOpenVersion() {
		return openVersion;
	}

	public void setOpenVersion(int openVersion) {
		this.openVersion = openVersion;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Node getFather() {
		return father;
	}
	public void setFather(Node father) {
		this.father = father;
	}
	
	@Override
	public String toString() {
		return "AStarNode [x=" + x + ", y=" + y + ", f=" + f + "]";
	}

	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}
	
	public Node[] getAroundNodes() {
		return aroundNodes;
	}
}
