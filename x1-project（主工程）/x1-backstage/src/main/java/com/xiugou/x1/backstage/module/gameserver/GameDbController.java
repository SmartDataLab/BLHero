/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.model.GameDb;
import com.xiugou.x1.backstage.module.gameserver.service.GameDbService;
import com.xiugou.x1.backstage.module.gameserver.vo.GameDbVo;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.apiutil.ApiUtil;

/**
 * @author YY
 *
 */
@Controller
public class GameDbController {

	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private GameDbService gameDbService;
	
	@ApiDocument("请求游戏服的数据库配置")
	@RequestMapping(value = GameApi.dbs)
	@ResponseBody
	public List<GameDbVo> dbs(@RequestParam("platformId") int platformId, @RequestParam("serverId") int serverId,
			@RequestParam("type") String type, @RequestParam("time") long time, @RequestParam("sign") String sign) {
		Map<String, Object> map = new HashMap<>();
		map.put("platformId", platformId);
		map.put("serverId", serverId);
		map.put("type", type);
		map.put("time", time);
		String signSource = ApiUtil.jointKeyValueToSource(map, applicationSettings.getBackstageKey(), "=", "&");
		String localSign = ApiUtil.buildSign(signSource);
		Asserts.isTrue(localSign.equals(sign), TipsCode.WHAT_DO_YOU_WANT_TO_DO);
		List<GameDb> gameDbs = gameDbService.dbs(platformId, serverId, type);
		List<GameDbVo> results = new ArrayList<>();
		for(GameDb gameDb : gameDbs) {
			results.add(new GameDbVo(gameDb));
		}
		return results;
	}
}
