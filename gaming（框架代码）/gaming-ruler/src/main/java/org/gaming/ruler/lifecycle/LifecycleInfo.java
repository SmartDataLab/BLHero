package org.gaming.ruler.lifecycle;

/**
 * 存活周期信息
 */
public class LifecycleInfo {
    /**
     * 生命周期排序值，顺序启动，倒序关闭
     */
    private final int order;
    /**
     * 名称
     */
    private final String name;

    private LifecycleInfo(String name, int order) {
    	this.name = name;
        this.order = order;
    }

    public static LifecycleInfo valueOf(String name, Priority priority, int ordinal) {
        return new LifecycleInfo(name, priority.getPriority() + ordinal);
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("LifecycleInfo{order=%s, name=%s}", order, name);
    }
}
