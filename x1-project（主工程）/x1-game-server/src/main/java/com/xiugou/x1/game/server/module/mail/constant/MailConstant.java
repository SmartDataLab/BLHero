package com.xiugou.x1.game.server.module.mail.constant;

/**
 * @author yh
 * @date 2023/7/11
 * @apiNote
 */
public class MailConstant {
    public static final String UNCONVERT = "SHOW" ;  //不需要任何转化 直接给前端就行
    public static final String ID_TO_NICK = "PLAYER"; //需要讲玩家ID转换成玩家名
    public static final String ID_TO_ITEM_NAME = "ITEM"; //需要讲道具ID 转话为道具名，但是服务端不需要操作
}
