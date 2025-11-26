/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.gaming.backstage.service.AbstractService;
import org.gaming.tool.ConsoleUtil;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.gaming.tool.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.dotdata.model.LoginDotData;
import com.xiugou.x1.backstage.module.dotdata.model.LoginDotDataStatistic;

/**
 * @author YY
 *
 */
@Service
public class LoginDotDataStatisticService extends AbstractService<LoginDotDataStatistic> {

	@Autowired
	private LoginDotDataService loginDotDataService;
	
	/**
	 * 跑打点统计数据
	 */
	protected void runForSaveStatistic() {
		logger.info("统计打点数据开始");
		List<LoginDotDataStatistic> statisticList = loginDotDataService.statistic(DateTimeUtil.currMillis());
		this.repository().getBaseDao().insertUpdate(statisticList);
		logger.info("统计打点数据结束");
	}
	
	@PostConstruct
	public void console() {
		ConsoleUtil.addFunction("loginDot", () -> {
			test();
		});
	}

	public void test() {
		List<LoginDotData> insertList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 30; j++) {
				LoginDotData loginDotData = new LoginDotData();
				loginDotData.setOpenId("AAA" + RandomUtil.closeClose(1, 500));
				loginDotData.setChannelId(2);
				long nowTime = DateTimeUtil.currMillis() - DateTimeUtil.ONE_DAY_MILLIS * j;
				loginDotData.setDateStr(DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, nowTime));
				loginDotData.setTime(LocalDateTimeUtil.ofEpochMilli(nowTime));
				loginDotData.setTiming(RandomUtil.closeClose(1, 6));
				insertList.add(loginDotData);
			}
		}
		loginDotDataService.insertAll(insertList);
		for (int j = 0; j < 30; j++) {
			List<LoginDotDataStatistic> statisticList = loginDotDataService
					.statistic(DateTimeUtil.currMillis() - DateTimeUtil.ONE_DAY_MILLIS * j);
			this.repository().getBaseDao().insertUpdate(statisticList);
		}
	}
}
