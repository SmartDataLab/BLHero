/**
 * 
 */
package org.gaming.ruler.interesting.astar;

/**
 * @author YY
 * 
 * A星算法预处理器
 */
public interface AStarPreProcessor<E> {
	
	public <Node extends BaseAStarNode<E, Node>> boolean process(Node startNode, Node targetNode);
}
