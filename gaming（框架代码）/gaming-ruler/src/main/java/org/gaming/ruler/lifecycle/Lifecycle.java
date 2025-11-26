package org.gaming.ruler.lifecycle;

/**
 * 存活周期
 */
public interface Lifecycle {

    /**
     * 生命周期信息
     */
    default LifecycleInfo getInfo() {
    	return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.MEDIUM, Ordinal.MAX);
    }

    /**
     * 游戏起服时执行
     */
    default void start() throws Exception {
    }

    /**
     * 停服之前执行
     */
    default void stop() throws Exception {
    }

}
