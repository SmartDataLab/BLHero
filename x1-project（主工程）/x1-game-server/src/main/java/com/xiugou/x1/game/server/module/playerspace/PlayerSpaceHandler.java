/**
 * 
 */
package com.xiugou.x1.game.server.module.playerspace;

import java.util.HashMap;
import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.GiftCodeCache;
import com.xiugou.x1.design.module.autogen.GiftCodeAbstractCache.GiftCodeCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.backstage.BackstagePoster;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.playerspace.PlayerSpace.GiftCodeUseRequest;
import pb.xiugou.x1.protobuf.playerspace.PlayerSpace.GiftCodeUseResponse;
import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.giftcode.GiftCodeResponse;

/**
 * @author YY
 *
 */
@Controller
public class PlayerSpaceHandler {

	@Autowired
	private GiftCodeCache giftCodeCache;
	@Autowired
	private BackstagePoster backstagePoster;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private ThingService thingService;

	@PlayerCmd
	public GiftCodeUseResponse useCode(PlayerContext playerContext, GiftCodeUseRequest request) {
		Player player = playerService.getEntity(playerContext.getId());

		Map<String, Object> parameter = new HashMap<>();
		parameter.put("channelId", player.getCreateChannel());
		parameter.put("code", request.getCode());
		parameter.put("playerId", player.getId());
		parameter.put("playerName", player.getNick());
		String response = backstagePoster.formPost(GameApi.canUseCode, parameter);
		Asserts.isTrue(response != null, TipsCode.CODE_UNUSABLE);

		GiftCodeResponse data = GsonUtil.parseJson(response, GiftCodeResponse.class);
		if (data.getTipsCode() == 1) {
			Asserts.isTrue(false, TipsCode.CODE_NOT_FOUND);
		}
		if (data.getTipsCode() == 2) {
			Asserts.isTrue(false, TipsCode.CODE_USED);
		}
		if (data.getTipsCode() == 3) {
			Asserts.isTrue(false, TipsCode.CODE_USE_SAME_TYPE);
		}
		GiftCodeCfg giftCodeCfg = giftCodeCache.getOrThrow(data.getGiftConfigId());
		
		long startTime = DateTimeUtil.stringToMillis(DateTimeUtil.YYYY_MM_DD, giftCodeCfg.getStartTime());
		long endTime = DateTimeUtil.stringToMillis(DateTimeUtil.YYYY_MM_DD, giftCodeCfg.getEndTime());
		long now = DateTimeUtil.currMillis();
		Asserts.isTrue(startTime <= now && now <= endTime, TipsCode.CODE_EXPIRE);
		
		thingService.add(playerContext.getId(), giftCodeCfg.getRewards(), GameCause.GIFTCODE_USE);
		
		return GiftCodeUseResponse.getDefaultInstance();
	}
}
