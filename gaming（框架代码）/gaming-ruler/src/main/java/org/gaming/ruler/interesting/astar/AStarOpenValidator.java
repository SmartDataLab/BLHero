/**
 * 
 */
package org.gaming.ruler.interesting.astar;

/**
 * @author YY
 * 
 * A星算法open表添加验证器
 */
public interface AStarOpenValidator<T> {

	/**
	 * @param nextNode
	 * @param currentNode
	 * @param startNode
	 * @param targetNode
	 * @return true验证通过，false验证不通过
	 */
	public boolean validate(T nextNode, T currentNode, T startNode, T targetNode);
}
