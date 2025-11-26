/**
 * 
 */
package org.gaming.ruler.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class EventTraceNode {

	private final EventTraceNode parent;
	
	private final Class<?> eventClazz;
	
	private final List<EventTraceNode> childs = new ArrayList<>();
	
	private String cause;

	public EventTraceNode(Class<?> eventClazz, EventTraceNode parent) {
		this.eventClazz = eventClazz;
		this.parent = parent;
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		if (stacks.length > 3) {
			this.cause = stacks[2].getFileName() + ":" + stacks[2].getLineNumber() + "." + stacks[2].getMethodName();
		} else if (stacks.length > 2) {
			this.cause = stacks[1].getFileName() + ":" + stacks[1].getLineNumber() + "." + stacks[1].getMethodName();
		}
	}

	public Class<?> getEventClazz() {
		return eventClazz;
	}

	public List<EventTraceNode> getChilds() {
		return childs;
	}

	public EventTraceNode getParent() {
		return parent;
	}
	
	public static String tab = "\t";
	
	public void print() {
		StringBuilder builder = new StringBuilder();
		builder.append(eventClazz.getSimpleName() + " (post in " + this.cause + ")");
		for(EventTraceNode child : childs) {
			printNode(builder, child, "");
		}
		System.out.println(builder.toString());
	}
	private void printNode(StringBuilder builder, EventTraceNode child, String tab) {
		builder.append("\n");
		String currTab = tab + EventTraceNode.tab;
		builder.append(currTab).append(child.eventClazz.getSimpleName() + " (post in " + child.cause + ")");
		for(EventTraceNode subChild : child.childs) {
			printNode(builder, subChild, currTab);
		}
	}
}
