/**
 * 
 */
package com.xiugou.x1.backstage.module.giftcode;

import java.util.ArrayList;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.service.UserService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.giftcode.form.GiftCodeForm;
import com.xiugou.x1.backstage.module.giftcode.model.GiftCode;
import com.xiugou.x1.backstage.module.giftcode.model.GiftCodeCfg;
import com.xiugou.x1.backstage.module.giftcode.model.GiftCodeShareLog;
import com.xiugou.x1.backstage.module.giftcode.service.GiftCodeCfgService;
import com.xiugou.x1.backstage.module.giftcode.service.GiftCodeService;
import com.xiugou.x1.backstage.module.giftcode.service.GiftCodeShareLogService;
import com.xiugou.x1.backstage.module.giftcode.struct.GiftCodeQuery;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.giftcode.GiftCodeResponse;

/**
 * @author YY
 *
 */
@Controller
public class GiftCodeController {

	@Autowired
	private GiftCodeService giftCodeService;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private UserService userService;
	@Autowired
	private GiftCodeShareLogService giftCodeShareLogService;
	@Autowired
	private GiftCodeCfgService giftCodeCfgService;
	
	
	@ApiDocument("请求礼包码数据")
	@RequestMapping(value = "/giftCode/data.auth")
	@ResponseBody
	public PageData<GiftCode> data(GiftCodeQuery query) {
		QuerySet querySet = new QuerySet();
		if(query.getConfigId() != 0) {
			querySet.addCondition("config_id = ?", query.getConfigId());
		}
		if(query.getCode() != null && !"".equals(query.getCode())) {
			querySet.addCondition("code = ?", query.getCode());
		}
		if(query.getPlayerId() != 0) {
			querySet.addCondition("player_id = ?", query.getPlayerId());
		}
		if(query.getPlayerName() != null && !"".equals(query.getPlayerName())) {
			querySet.addCondition("player_name like ?", "%" + query.getPlayerName() + "%");
		}
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return giftCodeService.query(querySet);
	}
	
	@ApiDocument("新增礼包码数据")
	@RequestMapping(value = "/giftCode/save.authw")
	@ResponseBody
	public void save(GiftCodeForm form) {
		long currChannel = gameChannelService.currChannel();
		RoleContext roleContext = userService.getCurrUser();
		
		if(form.getType() == 1) {
			GiftCodeCfg giftCodeCfg = giftCodeCfgService.getById(form.getConfigId());
			
			GiftCode giftCode = new GiftCode();
			giftCode.setChannelId(currChannel);
			giftCode.setType(1);
			giftCode.setConfigId(form.getConfigId());
			giftCode.setConfigName(giftCodeCfg == null ? "" : giftCodeCfg.getName());
			giftCode.setUserId(roleContext.getId());
			giftCode.setUserName(roleContext.getName());
			giftCode.setCode(form.getCode());
			giftCodeService.insert(giftCode);
		} else {
			GiftCodeCfg giftCodeCfg = giftCodeCfgService.getById(form.getConfigId());
			
			List<GiftCode> giftCodes = new ArrayList<>();
			for(int i = 0; i < form.getNum(); i++) {
				GiftCode giftCode = new GiftCode();
				giftCode.setChannelId(currChannel);
				giftCode.setType(2);
				giftCode.setConfigId(form.getConfigId());
				giftCode.setConfigName(giftCodeCfg == null ? "" : giftCodeCfg.getName());
				giftCode.setUserId(roleContext.getId());
				giftCode.setUserName(roleContext.getName());
				giftCode.setCode(randomCode(8));
				giftCodes.add(giftCode);
				if(giftCodes.size() >= 100) {
					tryInsertCodes(giftCodes);
					giftCodes.clear();
				}
			}
			if(!giftCodes.isEmpty()) {
				tryInsertCodes(giftCodes);
				giftCodes.clear();
			}
		}
	}
	
	private void tryInsertCodes(List<GiftCode> giftCodes) {
		//先尝试批量插入，批量插入失败则逐个插入
		try {
			//分批插入礼包码，存在随机码重复的情况
			giftCodeService.insertAll(giftCodes);
		} catch (Exception e) {
			for(GiftCode temp : giftCodes) {
				try {
					giftCodeService.insert(temp);
				} catch (Exception e2) {
					//重复的礼包码丢弃掉
				}
			}
		}
	}
	
	private String randomCode(int codeBit) {
		String code = "";
		for(int i = 0; i < codeBit; i++) {
			int index = RandomUtil.closeOpen(0, letters.length);
			code += letters[index];
		}
		return code;
	}
	
	
	private static char[] letters = new char[] {
			'2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','J','K','M','N','P','Q','R','S','T','U','V','W','X','Y','Z',
	};
	
	
	@ApiDocument("礼包码是否可用，给游戏服的接口")
	@RequestMapping(value = GameApi.canUseCode)
	@ResponseBody
	public String canUseCode(@RequestParam("channelId") int channelId, @RequestParam("code") String code,
			@RequestParam("playerId") long playerId, @RequestParam("playerName") String playerName) {
		GiftCodeResponse response = new GiftCodeResponse();
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", channelId);
		querySet.addCondition("code = ?", code);
		querySet.formWhere();
		PageData<GiftCode> giftCodes = giftCodeService.query(querySet);
		if(giftCodes.getData().isEmpty()) {
			//兑换码无效
			response.setTipsCode(1);
			return GsonUtil.toJson(response);
		}
		GiftCode giftCode = giftCodes.getData().get(0);
		if(giftCode.getType() == 1) {
			//查通码使用日志
			QuerySet logQuerySet = new QuerySet();
			logQuerySet.addCondition("player_id = ?", playerId);
			logQuerySet.addCondition("code = ?", code);
			logQuerySet.formWhere();
			PageData<GiftCodeShareLog> giftCodeShareLogs = giftCodeShareLogService.query(logQuerySet);
			if(!giftCodeShareLogs.getData().isEmpty()) {
				//该玩家已经使用过通码
				response.setTipsCode(3);
				return GsonUtil.toJson(response);
			}
			GiftCodeShareLog log = new GiftCodeShareLog();
			log.setCode(code);
			log.setPlayerId(playerId);
			log.setPlayerName(playerName);
			giftCodeShareLogService.insert(log);
			
			response.setGiftConfigId(giftCode.getConfigId());
			return GsonUtil.toJson(response);
		} else {
			if(giftCode.getPlayerId() != 0) {
				response.setTipsCode(2);
				return GsonUtil.toJson(response);
			}
			QuerySet useQuerySet = new QuerySet();
			useQuerySet.addCondition("channel_id = ?", channelId);
			useQuerySet.addCondition("config_id = ?", giftCode.getConfigId());
			useQuerySet.addCondition("player_id = ?", playerId);
			useQuerySet.formWhere();
			PageData<GiftCode> useGiftCodes = giftCodeService.query(useQuerySet);
			if(!useGiftCodes.getData().isEmpty()) {
				//已经使用过同类礼包
				response.setTipsCode(3);
				return GsonUtil.toJson(response);
			}
			giftCode.setPlayerId(playerId);
			giftCode.setPlayerName(playerName);
			giftCodeService.updateUnuse(giftCode);
			
			response.setGiftConfigId(giftCode.getConfigId());
			return GsonUtil.toJson(response);
		}
	}
}
