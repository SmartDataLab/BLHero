/**
 *
 */
package com.xiugou.x1.game.server.module.recharge.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.ruler.spring.Spring;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;

/**
 * @author YY
 */
@Repository
@Table(name = "recharge", comment = "充值表", dbAlias = "game")
public class Recharge extends AbstractEntity {
    @Id(strategy = Strategy.IDENTITY)
    @Column(comment = "唯一ID")
    private String id;
    @Column(name = "player_id", comment = "玩家ID", readonly = true)
    private long playerId;
    @Column(comment = "玩家名称", readonly = true)
    private String nick;
    @Column(name = "player_lv", comment = "玩家下单时的等级", readonly = true)
    private int playerLv;
    @Column(name = "product_id", comment = "商品ID", readonly = true)
    private int productId;
    @Column(name = "product_name", comment = "商品名称", readonly = true)
    private String productName;
    @Column(name = "product_code", comment = "商品编码", readonly = true)
    private String productCode;
    @Column(name = "server_id", comment = "服务器ID", readonly = true)
    private int serverId;
    @Column(name = "pre_pay_money", comment = "预付金额", readonly = true)
    private long prePayMoney;


    @Column(name = "pay_money", comment = "实付金额")
    private long payMoney;
    @Column(name = "status", comment = "发货状态，0未处理，1成功，2失败")
    private int status;
    @Column(name = "give_time", comment = "发货时间")
    private LocalDateTime giveTime = LocalDateTime.now();
    @Column(name = "backstage_order_id", comment = "回调时的后台订单号")
    private String backstageOrderId;
    @Column(name = "extra_info", comment = "透传参数")
    private String extraInfo;
    @Column(name = "product_data", comment = "商品数据，由客户端传递", readonly = true, length = 1000)
    private String productData;
    @Column(name = "test", comment = "是否测试订单")
    private boolean test;
    @Column(comment = "是否虚拟订单（GM订单）", readonly = true)
    private boolean virtual;
    @Column(name = "game_cause", comment = "游戏事件", readonly = true)
    private int gameCause;
    @Column(name = "game_cause_text", comment = "游戏事件", readonly = true)
    private String gameCauseText;

    public int getPlayerLv() {
        return playerLv;
    }

    public void setPlayerLv(int playerLv) {
        this.playerLv = playerLv;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public long getPrePayMoney() {
        return prePayMoney;
    }

    public void setPrePayMoney(long prePayMoney) {
        this.prePayMoney = prePayMoney;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getGiveTime() {
        return giveTime;
    }

    public void setGiveTime(LocalDateTime giveTime) {
        this.giveTime = giveTime;
    }

    public String getBackstageOrderId() {
        return backstageOrderId;
    }

    public void setBackstageOrderId(String backstageOrderId) {
        this.backstageOrderId = backstageOrderId;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getProductData() {
        return productData;
    }

    public void setProductData(String productData) {
        this.productData = productData;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public int getGameCause() {
        return gameCause;
    }

    public void setGameCause(int gameCause) {
        this.gameCause = gameCause;
    }

    public String getGameCauseText() {
        return gameCauseText;
    }

    public void setGameCauseText(String gameCauseText) {
        this.gameCauseText = gameCauseText;
    }
    
    @Override
	public long idGenerator() {
		ApplicationSettings applicationSettings = Spring.getBean(ApplicationSettings.class);
		long idBase = applicationSettings.getGameServerPlatformid() * 10000 + applicationSettings.getGameServerId();
		//js精确的最大整数是2的53次方，因此玩家ID偏移不能太大
		return idBase << 25;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
