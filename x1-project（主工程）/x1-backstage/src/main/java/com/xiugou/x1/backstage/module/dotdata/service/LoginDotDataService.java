/**
 * 
 */
package com.xiugou.x1.backstage.module.dotdata.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.dotdata.model.LoginDotData;
import com.xiugou.x1.backstage.module.dotdata.model.LoginDotDataStatistic;
import com.xiugou.x1.backstage.module.dotdata.vo.LoginDotDataStatisticVo;

import pojo.xiugou.x1.pojo.module.player.form.LoginDotTiming;

/**
 * @author YY
 *
 */
@Service
public class LoginDotDataService extends AbstractService<LoginDotData> {

	private Queue<LoginDotData> dotData = new ConcurrentLinkedQueue<>();

	public void addLoginDot(long channelId, String openId, int timing) {
		LoginDotData loginDot = new LoginDotData();
		loginDot.setChannelId(channelId);
		loginDot.setOpenId(openId);
		loginDot.setTiming(timing);
		loginDot.setDateStr(DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, DateTimeUtil.currMillis()));
		dotData.add(loginDot);
	}

	public void addLoginDot(LoginDotData loginDot) {
		dotData.add(loginDot);
	}

	/**
	 * 保存打点数据记录
	 */
	protected void runForSaveDot() {
		LoginDotData loginDot = dotData.poll();
		List<LoginDotData> insertList = new ArrayList<>();
		while (loginDot != null) {
			insertList.add(loginDot);
			if (insertList.size() >= 500) {
				this.insertAll(insertList);
				insertList.clear();
			}
			loginDot = dotData.poll();
		}
		if (!insertList.isEmpty()) {
			this.insertAll(insertList);
		}
	}

	public List<LoginDotDataStatistic> statistic(long millisTime) {
		try {
			String tableMonth = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMM, millisTime);
			String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, millisTime);

			QuerySet querySet = new QuerySet();
			querySet.addCondition("date_str = ?", dateStr);
			querySet.groupBy("GROUP BY channel_id, date_str, timing");
			querySet.formWhere();
			String sql = "SELECT channel_id as channelId, date_str as dateStr, timing, count(DISTINCT open_id) as num FROM `dot_data_login_"
					+ tableMonth + "`" + querySet.getWhere();
			List<LoginDotDataStatisticVo> voList = this.repository().getBaseDao()
					.queryAliasObjects(LoginDotDataStatisticVo.class, sql, querySet.getParams());

			Map<Long, LoginDotDataStatistic> channelStatistic = new HashMap<>();

			for (LoginDotDataStatisticVo vo : voList) {
				LoginDotDataStatistic statistic = channelStatistic.get(vo.getChannelId());
				if (statistic == null) {
					statistic = new LoginDotDataStatistic();
					statistic.setChannelId(vo.getChannelId());
					statistic.setDateStr(dateStr);
					channelStatistic.put(statistic.getChannelId(), statistic);
				}
				if (vo.getTiming() == LoginDotTiming.REQ_RES_URL_BEGIN.getValue()) {
					statistic.setReqResUrlBegin((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_RES_URL_SUCESS.getValue()) {
					statistic.setReqResUrlSucess((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_RES_URL_FAIL.getValue()) {
					statistic.setReqResUrlFail((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.SDK_LOGIN_BEGIN.getValue()) {
					statistic.setSdkLoginBegin((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.SDK_LOGIN_SUCESS.getValue()) {
					statistic.setSdkLoginSucess((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.SDK_LOGIN_FAIL.getValue()) {
					statistic.setSdkLoginFail((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SERVER_VERIFY_BEGIN.getValue()) {
					statistic.setReqServerVerifyBegin((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SERVER_VERIFY_SUCESS.getValue()) {
					statistic.setReqServerVerifySucess((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SERVER_VERIFY_FAIL.getValue()) {
					statistic.setReqServerVerifyFail((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SERVER_LIST_BEGIN.getValue()) {
					statistic.setReqServerListBegin((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SERVER_LIST_SUCESS.getValue()) {
					statistic.setReqServerListSucess((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SERVER_LIST_FAIL.getValue()) {
					statistic.setReqServerListFail((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.START_GAME.getValue()) {
					statistic.setStartGame((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SOCKET_CONNECT_SUCESS.getValue()) {
					statistic.setReqSocketConnectSucess((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SOCKET_LOGIN_BEGIN.getValue()) {
					statistic.setReqSocketLoginBegin((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SOCKET_LOGIN_SUCESS.getValue()) {
					statistic.setReqSocketLoginSucess((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SOCKET_LOGIN_FAIL.getValue()) {
					statistic.setReqSocketLoginFail((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.CREATE_SUCC.getValue()) {
					statistic.setCreateSucc((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.REQ_SERVER_PUSH_DATA_END.getValue()) {
					statistic.setReqServerPushDataEnd((int) vo.getNum());
				} else if (vo.getTiming() == LoginDotTiming.IN_GAME.getValue()) {
					statistic.setInGame((int) vo.getNum());
				}
			}
			return new ArrayList<>(channelStatistic.values());
		} catch (Exception e) {
			logger.error("统计登录打点异常", e);
			return Collections.emptyList();
		}
	}
}
