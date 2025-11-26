/**
 * 
 */
package org.gaming.ruler.interesting.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.ruler.interesting.astar.BinaryHeap.HeapComparator;

/**
 * @author YY
 *
 */
public abstract class AStar<E, Node extends BaseAStarNode<E, Node>> {
	/**
	 * open表
	 */
	protected BinaryHeap<Node> openList;
	/**
	 * g值计算附加器
	 */
	protected AStarGvalueAdaptor<E> gvalueAdaptor;
	/**
	 * open表添加规则验证器
	 */
	protected AStarOpenValidator<E> openValidator;
	/**
	 * a星预处理器
	 */
	protected AStarPreProcessor<E> preProcessor;
	/**
	 * H值基数
	 */
	protected final int HBASE = 100;
	/**
	 * G值基数
	 */
	protected final int GBASE = HBASE / 10;
	
	protected int version = 0;
	
	//直线G
	protected final int STRAIGHT = (int)(1.0f * GBASE);
	//斜线G
	protected final int OBLIQUE = (int)(1.4f * GBASE);
	
	/**
	 * 已检测的节点数
	 */
	protected int lookAhead;
	/**
	 * 检测过的节点
	 */
	protected List<Node> lookNodes;
	
	protected Node[][] nodeMatrix;

	protected List<Node> allNodeList;
	
	public AStar(E[][] entityMatrix) {
		this.openList = new BinaryHeap<Node>(50, astarComparator);
		this.lookNodes = new ArrayList<>(50);
		
		int height = entityMatrix.length;
		int width = entityMatrix[0].length;
		
		this.allNodeList = new ArrayList<>(height * width);
		
		this.nodeMatrix = createNodeMatrix(width, height);
		
		int nodeId = 1;
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				Node node = createNode(nodeId, x, y, entityMatrix[y][x]);
				this.nodeMatrix[y][x] = node;
				this.allNodeList.add(node);
				nodeId++;
			}
		}
		initArroundNodes();
	}
	
	protected abstract Node[][] createNodeMatrix(int width, int height);
	protected abstract Node createNode(int nodeId, int x, int y, E entity);
	
	private void initArroundNodes() {
		for (Node node : allNodeList) {
			initArroundNodes(node);
		}
	}
	protected abstract void initArroundNodes(Node node);
	
	/**
	 * 将寻路过程中的某个点添加到open表中
	 * @param aroundNode 正在检查的点，周围的点
	 * @param currentNode 当前点
	 * @param startNode 起点
	 * @param targetNode 目标点
	 */
	protected void addOpenList(Node aroundNode, Node currentNode, Node startNode, Node targetNode) {
		if(aroundNode.getCloseVersion() == version) {
			//在close表中直接跳过
			return;
		}
		if(openValidator != null) {
			if(!openValidator.validate(aroundNode.getEntity(), currentNode.getEntity(), startNode.getEntity(), targetNode.getEntity())) {
				return;
			}
		}
		//TODO 1的定义
		int costG = costG(aroundNode, currentNode);
		
		int g = currentNode.getG() + costG;
		int h = euclid(aroundNode, targetNode);
		int f = g + h;
		
		if(aroundNode.getOpenVersion() == version) {
			if(aroundNode.getF() > f) {
				aroundNode.setFather(currentNode);
				//计算h,g,f值
				aroundNode.setH(h);
				aroundNode.setG(g);
				aroundNode.setF(f);
			}
		} else {
			aroundNode.setFather(currentNode);
			//计算h,g,f值
			aroundNode.setH(h);
			aroundNode.setG(g);
			aroundNode.setF(f);
			
			openList.add(aroundNode);
//			openList.print();
			aroundNode.setOpenVersion(version);
		}
	}
	
	protected abstract int costG(Node aroundNode, Node currentNode);
	
	/**
	 * A星H值的计算，不同需求的计算不一样
	 * 曼哈顿式
	 * @param node1
	 * @param node2
	 * @return
	 */
	protected int manhattan(Node node1, Node node2) {
		int dx = node1.getX() - node2.getX();
		int dy = node1.getY() - node2.getY();
		return (Math.abs(dx) + Math.abs(dy)) * HBASE;
	}
	/**
	 * A星H值的计算，不同需求的计算不一样
	 * 欧几里得式，采用这种方式的时候，稍微放大HBase会使结果更加准确
	 * @param node1
	 * @param node2
	 * @return
	 */
	protected int euclid(Node node1, Node node2) {
		int dx = node1.getX() - node2.getX();
		int dy = node1.getY() - node2.getY();
		return (int)(Math.sqrt(dx * dx + dy *dy) * HBASE);
	}
	
	public void setGvalueAdaptor(AStarGvalueAdaptor<E> gvalueAdaptor) {
		this.gvalueAdaptor = gvalueAdaptor;
	}
	public void setOpenValidator(AStarOpenValidator<E> openValidator) {
		this.openValidator = openValidator;
	}
	public void setPreProcessor(AStarPreProcessor<E> preProcessor) {
		this.preProcessor = preProcessor;
	}
	
	protected final HeapComparator<Node> astarComparator = new HeapComparator<Node>() {
		@Override
		public boolean compare(Node n1, Node n2) {
			return n1.getF() < n2.getF();
		}
	};
	
	protected List<E> formatPath(Node targetNode) {
		List<E> path = new ArrayList<>();
		Node pathNode = targetNode;
		while(pathNode != null) {
			path.add(pathNode.getEntity());
			pathNode = pathNode.getFather();
		}
		Collections.reverse(path);
		return path;
	}
	
	protected Node getNode(int x, int y) {
		return nodeMatrix[y][x];
	}
	
	/**
	 * A星寻路
	 * @param startX
	 * @param startY
	 * @param targetX
	 * @param targetY
	 * @return 如果找到路径则返回目标坐标的对象，找不到则返回null
	 */
	public Node searchPathNode(int startX, int startY, int targetX, int targetY, int maxCheck) {
		//此刻开始节点是当前节点
		Node startNode = getNode(startX, startY);
		Node currentNode = startNode;
		Node targetNode = getNode(targetX, targetY);
		if(startNode == null || targetNode == null) {
			return null;
		}
		//A星算法的预处理
		if(preProcessor != null) {
			if(!preProcessor.process(currentNode, targetNode)) {
				return null;
			}
		}
		//重置A星的状态
		openList.clear();
		//使用版本号来检测节点的开关状态
		version++;
		//重置起点的启发值
		startNode.setG(0);
		startNode.setH(euclid(startNode, targetNode));
		startNode.setF(startNode.getH() + startNode.getG());
		startNode.setFather(null);
		startNode.setOpenVersion(version);
		//已检测数量
		lookAhead = 0;
		lookNodes.clear();
		//
		while(currentNode.getId() != targetNode.getId()) {
			currentNode.setCloseVersion(version);
			lookAhead++;
			//检查周围的节点
			for (int i = 0; i < currentNode.getAroundNodes().length; i++) {
				Node aroundNode = currentNode.getAroundNodes()[i];
				addOpenList(aroundNode, currentNode, startNode, targetNode);
			}
			if (openList.size() <= 0) {
				return getHFGMinNode();
			}
			//已达到最大检测次数，则返回HFG值最小的节点
			if(maxCheck != 0 && lookAhead >= maxCheck) {
				return getHFGMinNode();
			}
			currentNode = openList.poll();
			lookNodes.add(currentNode);
		}
		return targetNode;
	}
	
	
	private Node getHFGMinNode() {
		Node minNode = null;
		for (int i = 0; i < lookNodes.size(); i++) {
			Node node = lookNodes.get(i);
			if(minNode == null) {
				minNode = node;
				continue;
			}
			if (node.getH() < minNode.getH()) {
				minNode = node;
			} else if (node.getH() == minNode.getH()) {
				if (node.getF() < minNode.getF()) {
					minNode = node;
				} else if (node.getF() == minNode.getF()) {
					if (node.getG() < minNode.getG()) {
						minNode = node;
					}
				}
			}
		}
		return minNode;
	}
	
	public List<E> searchPath(int startX, int startY, int targetX, int targetY, int maxCheck) {
		Node targetNode = searchPathNode(startX, startY, targetX, targetY, maxCheck);
		if(targetNode != null) {
			return formatPath(targetNode);
		}
		return null;
	}
	
	public List<E> searchPath(int startX, int startY, int targetX, int targetY) {
		return searchPath(startX, startY, targetX, targetY, 0);
	}
}
