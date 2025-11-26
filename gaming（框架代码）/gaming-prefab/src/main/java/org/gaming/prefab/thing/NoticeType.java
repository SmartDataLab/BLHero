/**
 *
 */
package org.gaming.prefab.thing;

/**
 * @author YY
 *
 */
public enum NoticeType {
	/**
	 * 不做推送
	 */
	NO(0),
    /**
     * 沉默方式，更新前端不做任何显示
     */
    SLIENT(1),
    /**
     * 普通方式，更新前端并弹窗
     */
    NORMAL(2),
    /**
     * 飘字方式，更新前端并飘字
     */
    TIPS(3),
    /**
     * 后续完善
     */
    LAZY(999),
    ;
    private final int value;

    private NoticeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
