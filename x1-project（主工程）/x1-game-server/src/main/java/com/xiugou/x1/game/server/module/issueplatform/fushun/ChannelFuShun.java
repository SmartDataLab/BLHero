/**
 * 
 */
package com.xiugou.x1.game.server.module.issueplatform.fushun;

import java.util.HashMap;
import java.util.Map;

import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.util.HttpUtil;
import org.gaming.ruler.util.SensitiveUtil.SensitiveBuilder;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ChannelSdkArgsCache;
import com.xiugou.x1.game.server.module.player.difchannel.DifChannel;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.recharge.model.Recharge;

import pb.xiugou.x1.protobuf.player.Player.PbFuShunChannelData;
import pb.xiugou.x1.protobuf.recharge.Recharge.PbRechargeDataFuShun;
import pb.xiugou.x1.protobuf.recharge.Recharge.RechargeOrderResponse.Builder;
import pojo.xiugou.x1.pojo.apiutil.ApiUtil;

/**
 * 
 */
@Component
public class ChannelFuShun extends DifChannel {

	@Autowired
	private ChannelSdkArgsCache channelSdkArgsCache;
	
	@Override
	public String identity() {
		return "fushun";
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("game_id", "30128");
		map.put("code", "37d3d285986407495811d4d9ec1063a3");
		String signSource = ApiUtil.jointValueToSource(map, "da371d609efdf1bf9c7e9d8fff364af9");
		String sign = ApiUtil.buildSign(signSource);
		map.put("sign", sign);

		System.out.println(map);
		System.out.println("fushun签名原文：{}" + signSource);
		System.out.println("fushun登录签名：{}" + sign);
		String response = HttpUtil.formPost("https://api.hnfushun.com/server/code/", map);
		System.out.println("fushun登录验证响应：{}" + response);
		
//		用form-data post参数
	}
	
	@Override
	protected void checkLogin(String openId, ByteString channelData) {
		try {
			PbFuShunChannelData build = PbFuShunChannelData.parseFrom(channelData);
			
//			player=0 fushun签名原文：084ac691a9dbb2fb8f9f4e735a1ba2f6   30128   da371d609efdf1bf9c7e9d8fff364af9
//			player=0 fushun登录签名：17aec4c83cf40b103baa0e4ea3930949
//			player=0 fushun登录验证响应：{"state":0,"msg":"CODE\u4e0d\u80fd\u4e3a\u7a7a"}
			
			Map<String, Object> map = new HashMap<>();
			map.put("game_id", channelSdkArgsCache.getHql_game_id());
			map.put("code", build.getCode());
			String signSource = ApiUtil.jointValueToSource(map, channelSdkArgsCache.getHql_game_key());
			String sign = ApiUtil.buildSign(signSource);
			map.put("sign", sign);

			logger.info("fushun签名原文：{}", signSource);
			logger.info("fushun登录签名：{}，{}", sign, channelSdkArgsCache.getHql_login_url());
			String response = HttpUtil.formPost(channelSdkArgsCache.getHql_login_url(), map);
			logger.info("fushun登录验证响应：{}", response);

			FuShunLoginResponse loginResponse = GsonUtil.parseJson(response, FuShunLoginResponse.class);
			Asserts.isTrue(loginResponse.state == 1, TipsCode.PLAYER_LOGIN_CHANNEL_FAIL);
			Asserts.isTrue(openId.equals(loginResponse.data.uid), TipsCode.PLAYER_LOGIN_CHANNEL_FAIL);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	public static class FuShunLoginResponse {
		public int state;
		public String msg;
		public FuShunLoginData data;
	}
	public static class FuShunLoginData {
		public String token;
		public String uid;
	}

	@Override
	protected boolean checkName(Player player, String newNick, ByteString channelData) {
		return true;
	}

	@Override
	protected void onCreate(Player player, ByteString channelData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onLogin(Player player, ByteString channelData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRecharge(Recharge recharge, ByteString rechargeData, Builder response) {
		Player player = playerService.getEntity(recharge.getPlayerId());
		
		PbRechargeDataFuShun.Builder builder = PbRechargeDataFuShun.newBuilder();
		builder.setTotalFee(recharge.getPrePayMoney() + "");
		builder.setRoleId(player.getId());
		builder.setRoleName(player.getNick());
		builder.setServerId(player.getServerId());
		builder.setServerName("S" + player.getServerId());
		builder.setRoleLevel(player.getLevel());
		builder.setVipLevel(player.getVipLevel());
		builder.setProductId(recharge.getProductId());
		builder.setProductName(recharge.getProductName());
		builder.setCpOrderNum(recharge.getId());
		builder.setExt(recharge.getExtraInfo());
		
		response.setChannelIdentity(identity());
		response.setRechargeData(builder.build().toByteString());
	}

	@Override
	protected void loadSensitive(SensitiveBuilder builder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void changeName(Player player) {
		// TODO Auto-generated method stub
		
	}

}
