/**
 * 
 */
package com.xiugou.x1.game.server.module.issueplatform.wabo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.gaming.ruler.util.SensitiveUtil.SensitiveBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.xiugou.x1.design.module.ChannelSdkArgsCache;
import com.xiugou.x1.design.module.LanguageAutoCache;
import com.xiugou.x1.game.server.module.player.difchannel.DifChannel;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;

import pb.xiugou.x1.protobuf.recharge.Recharge.PbRechargeDataWabo;
import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeOrderResponse.Builder;

/**
 * @author YY
 *
 */
@Component
public class ChannelWabo extends DifChannel {

	@Autowired
	private ChannelSdkArgsCache channelSdkArgsCache;
	@Autowired
	private LanguageAutoCache languageAutoCache;
	
	@Override
	public String identity() {
		return "wabo";
	}

	@Override
	protected void checkLogin(String openId, ByteString channelData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean checkName(Player player, String newNick, ByteString channelData) {
		return true;
	}

	@Override
	protected void onCreate(Player player, ByteString channelData) {
		
	}

	@Override
	protected void onLogin(Player player, ByteString channelData) {
		
	}

	@Override
	protected void onRecharge(Recharge recharge, ByteString rechargeData, Builder response) {
		Player player = playerService.getEntity(recharge.getPlayerId());
		PbRechargeDataWabo.Builder builder = PbRechargeDataWabo.newBuilder();
		builder.setCallbackUrl(channelSdkArgsCache.getWabo_callback_url());
		String money = new BigDecimal(recharge.getPrePayMoney() + "").divide(new BigDecimal("100.0"), 2, RoundingMode.FLOOR).toString();
		builder.setAmount(money);
		builder.setRoleId(player.getId());
		builder.setRoleName(player.getNick());
		builder.setServerId(applicationSettings.getGameServerId());
		builder.setServerName("S" + applicationSettings.getGameServerId());
		String langKey = "RechargeProduct_Name_" + recharge.getProductId();
		String productName = languageAutoCache.getLang(applicationSettings.getGameLanguage(), langKey);
		builder.setGoodsDesc(productName);
		builder.setGoodsId(recharge.getProductCode());
		builder.setCpOrderId(recharge.getId());
		builder.setExt(recharge.getExtraInfo());
		
		response.setChannelIdentity(identity());
		response.setRechargeData(builder.build().toByteString());
	}

	@Override
	protected void loadSensitive(SensitiveBuilder builder) {
		
	}

	@Override
	protected void changeName(Player player) {
		
	}
}
