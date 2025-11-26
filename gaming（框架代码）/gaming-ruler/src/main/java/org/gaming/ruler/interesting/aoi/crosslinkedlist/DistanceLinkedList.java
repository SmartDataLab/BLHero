/**
 * 
 */
package org.gaming.ruler.interesting.aoi.crosslinkedlist;

import java.util.Map.Entry;

/**
 * @author YY
 *
 */
public class DistanceLinkedList<T> {

	private final Node head;
	
	private final Node tail;
	
	public DistanceLinkedList() {
		this.head = new Node() {};
		this.head.distance = Integer.MIN_VALUE;
		this.tail = new Node() {};
		this.tail.distance = Integer.MAX_VALUE;
		this.head.next = this.tail;
		this.tail.prev = this.head;
	}
	
	public void enter(EntityNode<T> entity, int distance) {
		entity.setDistance(distance);
		insertOnHead(entity);
		moveToRight(entity);
	}
	
	private void insertOnHead(EntityNode<T> entity) {
		entity.rightSentry.next = this.head.next;
		this.head.next.prev = entity.rightSentry;
		
		entity.leftSentry.prev = this.head;
		this.head.next = entity.leftSentry;
	}
	
	@SuppressWarnings("unchecked")
	private void moveToRight(EntityNode<T> entity) {
		boolean loop = true;
		while(loop) {
			boolean rightSentryMoved = entity.rightSentry.distance > entity.rightSentry.next.distance;
			if(rightSentryMoved) {
				Node oriRightNode = entity.rightSentry.next;
				cutLinkIn(entity.rightSentry);
				insertBetween(oriRightNode, entity.rightSentry, oriRightNode.next);
				//TODO 从左边越过，判断oriRightNode是不是一个实体，是则进入entity的观察集合
				if(oriRightNode instanceof EntityNode) {
					EntityNode<T> other = (EntityNode<T>)oriRightNode;
					entity.interestedIn(other);
				}
			}
			boolean entityMoved = entity.distance > entity.next.distance;
			if(entityMoved) {
				Node oriRightNode = entity.next;
				cutLinkIn(entity);
				insertBetween(oriRightNode, entity, oriRightNode.next);
				if(oriRightNode instanceof SentryNode) {
					SentryNode<T> sentry = (SentryNode<T>)oriRightNode;
					
					//TODO 从左边越过，判断oriRightNode是不是某个实体的左哨兵，是则进入该实体的集合
					if(sentry.entity.leftSentry == sentry) {
						sentry.entity.interestedIn(entity);
					}
					//TODO 从左边越过，判断oriRightNode是不是某个实体的右哨兵，是则离开该实体的集合
					if(sentry.entity.rightSentry == sentry) {
						sentry.entity.uninterestedIn(entity);
					}
				}
			}
			boolean leftSentryMoved = entity.leftSentry.distance > entity.leftSentry.next.distance;
			if(leftSentryMoved) {
				Node oriRightNode = entity.leftSentry.next;
				cutLinkIn(entity.leftSentry);
				insertBetween(oriRightNode, entity.leftSentry, oriRightNode.next);
				//TODO 从左边越过，判断oriRightNode是不是一个实体，是则离开entity的集合
				if(oriRightNode instanceof EntityNode) {
					EntityNode<T> other = (EntityNode<T>)oriRightNode;
					entity.uninterestedIn(other);
				}
			}
			loop = rightSentryMoved || entityMoved || leftSentryMoved;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void moveToLeft(EntityNode<T> entity) {
		boolean loop = true;
		while(loop) {
			boolean leftSentryMoved = entity.leftSentry.distance < entity.leftSentry.prev.distance;
			if(leftSentryMoved) {
				Node oriLeftNode = entity.leftSentry.prev;
				cutLinkIn(entity.leftSentry);
				insertBetween(oriLeftNode.prev, entity.leftSentry, oriLeftNode);
				//TODO 从右边越过，判断oriLeftNode是不是一个实体，是则进入entity的集合
				if(oriLeftNode instanceof EntityNode) {
					EntityNode<T> other = (EntityNode<T>)oriLeftNode;
					entity.interestedIn(other);
				}
			}
			
			boolean entityMoved = entity.distance < entity.prev.distance;
			if(entityMoved) {
				Node oriLeftNode = entity.prev;
				cutLinkIn(entity);
				insertBetween(oriLeftNode.prev, entity, oriLeftNode);
				
				if(oriLeftNode instanceof SentryNode) {
					SentryNode<T> sentry = (SentryNode<T>)oriLeftNode;
					
					//TODO 从右边越过，判断oriLeftNode是不是某个实体的右哨兵，是则进入该实体的集合
					if(sentry.entity.rightSentry == sentry) {
						sentry.entity.interestedIn(entity);
					}
					//TODO 从右边越过，判断oriRightNode是不是某个实体的左哨兵，是则离开该实体的集合
					if(sentry.entity.leftSentry == sentry) {
						sentry.entity.uninterestedIn(entity);
					}
				}
			}
			
			boolean rightSentryMoved = entity.rightSentry.distance < entity.rightSentry.prev.distance;
			if(rightSentryMoved) {
				Node oriLeftNode = entity.rightSentry.prev;
				cutLinkIn(entity.rightSentry);
				insertBetween(oriLeftNode.prev, entity.rightSentry, oriLeftNode);
				//TODO 从右边越过，判断oriLeftNode是不是一个实体，是则离开entity的观察集合
				if(oriLeftNode instanceof EntityNode) {
					EntityNode<T> other = (EntityNode<T>)oriLeftNode;
					entity.uninterestedIn(other);
				}
			}
			loop = rightSentryMoved || entityMoved || leftSentryMoved;
		}
	}
	
	public void move(EntityNode<T> entity, int moveTo) {
		if(entity.distance < moveTo) {
			//向右走
			entity.setDistance(moveTo);
			moveToRight(entity);
		} else if(entity.distance > moveTo) {
			//向左走
			entity.setDistance(moveTo);
			moveToLeft(entity);
		}
	}
	
	private void insertBetween(Node front, Node insert, Node behind) {
		front.next = insert;
		insert.prev = front;
		insert.next = behind;
		behind.prev = insert;
	}
	
	private void cutLinkIn(Node node) {
		node.prev.next = node.next;
		node.next.prev = node.prev;
	}
	
	public void exit(EntityNode<T> entity) {
		cutLinkIn(entity.prev);
		cutLinkIn(entity);
		cutLinkIn(entity.next);
		
		for(Entry<CllEntity<T>, EntityNode<T>> other : entity.observer.entrySet()) {
			//将当前节点从其他节点的观察列表中删除
			other.getValue().watching.remove(entity.host);
		}
		for(Entry<CllEntity<T>, EntityNode<T>> other : entity.watching.entrySet()) {
			//将当前节点从其他节点的被观察列表中删除
			other.getValue().observer.remove(entity.host);
		}
	}
	
	public void print() {
		Node curr = this.head;
		while(curr != null) {
			System.out.println(curr);
			curr = curr.next;
		}
	}
}
