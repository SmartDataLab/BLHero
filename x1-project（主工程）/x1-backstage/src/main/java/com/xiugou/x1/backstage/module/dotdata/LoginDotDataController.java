/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata;

import java.util.HashMap;
import java.util.Map;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.ruler.util.HttpUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.foundation.starting.ApplicationSettings;
import com.xiugou.x1.backstage.module.dotdata.model.LoginDotData;
import com.xiugou.x1.backstage.module.dotdata.model.LoginDotDataStatistic;
import com.xiugou.x1.backstage.module.dotdata.service.LoginDotDataService;
import com.xiugou.x1.backstage.module.dotdata.service.LoginDotDataStatisticService;
import com.xiugou.x1.backstage.module.dotdata.struct.LoginDotQuery;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.player.form.LoginTimingTable;
import pojo.xiugou.x1.pojo.module.player.form.LoginTimingTable.LoginTiming;

/**
 * @author YY
 *
 */
@Controller
public class LoginDotDataController {

	@Autowired
	private LoginDotDataService loginDotDataService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private GameChannelService gameChannelService;
	@Autowired
	private LoginDotDataStatisticService loginDotDataStatisticService;

	@ApiDocument("登录打点接口")
	@RequestMapping(value = "/loginDotData/data.auth")
	@ResponseBody
	public PageData<LoginDotDataStatistic> data(LoginDotQuery query) {
		long channelId = gameChannelService.currChannel();

		QuerySet querySet = new QuerySet();
		querySet.addCondition("channel_id = ?", channelId);
		long startTime = query.getStartTime() * 1000L;
		if (startTime > 0) {
			String startBorn = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, startTime);
			querySet.addCondition("date_str >= ?", startBorn);
		}
		long endTime = query.getEndTime() * 1000L;
		if (endTime > 0) {
			String endBorn = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime);
			querySet.addCondition("date_str <= ?", endBorn);
		}
		querySet.orderBy("order by date_str desc");
		querySet.formWhere();

		PageData<LoginDotDataStatistic> pageData = loginDotDataStatisticService.query(querySet);
		return pageData;
	}

	// 因为非主后台不跑定时器，并且主后台不对外提供访问，客户端上报的打点数据都是在非主后台，
	// 所以上报到非主后台的打点数据，需要转发到主后台中进行录入
	// http://192.168.1.5:9001/api/loginDot?channelId=1&openId=AAA&timing=1
	@ApiDocument("提供给游戏客户端使用，非主后台的登录打点接口")
	@RequestMapping(value = GameApi.loginDot)
	@ResponseBody
	public void loginDot(@RequestParam("channelId") long channelId, @RequestParam("openId") String openId,
			@RequestParam("timing") int timing) {
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("channelId", channelId);
		parameter.put("openId", openId);
		parameter.put("timing", timing);
		HttpUtil.formPost(applicationSettings.getBackstageMainUrl() + GameApi.loginDotMain, parameter);
	}

	@ApiDocument("主后台的登录打点接口")
	@RequestMapping(value = GameApi.loginDotMain)
	@ResponseBody
	public void loginDotMain(@RequestParam("channelId") long channelId, @RequestParam("openId") String openId,
			@RequestParam("timing") int timing) {
		loginDotDataService.addLoginDot(channelId, openId, timing);
	}
	
	@ApiDocument("提供给游戏服使用，上报登录打点接口")
	@RequestMapping(value = GameApi.loginDotReport)
	@ResponseBody
	public void loginDotReport(@RequestBody LoginTimingTable loginTimingTable) {
		for(LoginTiming loginTiming : loginTimingTable.getDatas()) {
			LoginDotData loginDot = new LoginDotData();
			loginDot.setChannelId(loginTiming.getChannelId());
			loginDot.setOpenId(loginTiming.getOpenId());
			loginDot.setTiming(loginTiming.getTiming());
			loginDot.setDateStr(DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, loginTiming.getTime()));
			loginDot.setTime(LocalDateTimeUtil.ofEpochMilli(loginTiming.getTime()));
			loginDotDataService.addLoginDot(loginDot);
		}
	}
}
