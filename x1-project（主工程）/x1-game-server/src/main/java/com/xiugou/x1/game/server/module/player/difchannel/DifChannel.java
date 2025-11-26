/**
 * 
 */
package com.xiugou.x1.game.server.module.player.difchannel;

import org.gaming.ruler.util.SensitiveUtil.SensitiveBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.ByteString;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;

import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeOrderResponse;

/**
 * @author YY
 * 对接不同渠道的个性要求
 */
public abstract class DifChannel {
	
	protected static Logger logger = LoggerFactory.getLogger(DifChannel.class);
	
	@Autowired
	protected ApplicationSettings applicationSettings;
	@Autowired
	protected PlayerService playerService;
	
	public DifChannel() {
		DifChannelManager.register(this);
	}
	
	/**
	 * 渠道标识
	 * @return
	 */
	public abstract String identity();
	
	/**
	 * 验证登录
	 * @param player
	 * @param channelData
	 */
	protected abstract void checkLogin(String openId, ByteString channelData);
	/**
	 * 检查创角名字
	 * @param name
	 * @param channelIdentity
	 */
	protected abstract boolean checkName(Player player, String newNick, ByteString channelData);
	/**
	 * 创号后调用
	 * @param player
	 */
	protected abstract void onCreate(Player player, ByteString channelData);
	/**
	 * 登录后调用
	 * @param player
	 */
	protected abstract void onLogin(Player player, ByteString channelData);
	/**
	 * 充值验证
	 * @param player
	 * @param channelData
	 */
	protected abstract void onRecharge(Recharge recharge, ByteString rechargeData, RechargeOrderResponse.Builder response);
	/**
	 * 加载敏感字
	 */
	protected abstract void loadSensitive(SensitiveBuilder builder);
	/**
	 * 修改名字时上报数据
	 * @param player
	 */
	protected abstract void changeName(Player player);
}
