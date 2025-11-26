package com.xiugou.x1.game.server.module.bag.service;

import java.util.HashMap;
import java.util.Map;

import com.xiugou.x1.design.constant.UseItemType;


/**
 * @author yh
 * @date 2023/7/31
 * @apiNote
 */
public abstract class AbsUseItemService {

    public static Map<UseItemType, AbsUseItemService> useItem = new HashMap<>();

    public void register(AbsUseItemService service) {
        useItem.put(service.useItemType(), service);
    }

    public AbsUseItemService() {
        this.register(this);
    }

    public static AbsUseItemService getService(UseItemType type) {
        return useItem.get(type);
    }

    /**
     * 道具使用类型
     *
     * @return
     */
    public abstract UseItemType useItemType();

    /**
     * 使用道具
     *
     * @param pid
     * @param itemId
     * @param num
     * @param option
     */
    public abstract void use(long pid, int itemId, long num, int option);


}
