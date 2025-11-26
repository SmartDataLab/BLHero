/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gamecause.model.GameCause;
import com.xiugou.x1.backstage.module.gamecause.service.GameCauseService;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.player.struct.ConsumeData;
import com.xiugou.x1.backstage.module.player.struct.ConsumeQuery;
import com.xiugou.x1.backstage.module.player.vo.ConsumeLogVo;

import pojo.xiugou.x1.pojo.log.home.MeatLog;
import pojo.xiugou.x1.pojo.log.home.MineLog;
import pojo.xiugou.x1.pojo.log.home.WoodLog;
import pojo.xiugou.x1.pojo.log.player.DiamondLog;
import pojo.xiugou.x1.pojo.log.player.GoldLog;

/**
 * @author YY
 *
 */
@Controller
public class ConsumeController {

	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private GameCauseService gameCauseService;
	
	
	@ApiDocument("获取游戏消费统计数据")
    @RequestMapping(value = "/consume/data.auth")
    @ResponseBody
	public PageData<ConsumeLogVo> data(ConsumeQuery query) {
		Asserts.isTrue(query.getServerUid() > 0, TipsCode.GAME_SERVER_MISS, query.getServerUid());
		
		long startTime = query.getStartTime() * 1000L;
		if(startTime <= 0) {
			startTime = DateTimeUtil.monthZeroMillis();
		}
		String startStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, startTime);
		
		long endTime = query.getEndTime() * 1000L;
		if(endTime <= 0) {
			endTime = DateTimeUtil.nextMonthZeroMillis();
		}
		String endStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, endTime);
		
		DataBase dataBase = dataBaseManager.getLogDb(query.getServerUid());
		int yearMonthTime = query.getStartTime();
		if(yearMonthTime <= 0) {
			yearMonthTime = DateTimeUtil.currSecond();
		}
		
		Class<?> clazz = null;
		if(query.getResourceType() == 1) {//金币
			clazz = GoldLog.class;
		} else if(query.getResourceType() == 2) {//钻石、仙玉
			clazz = DiamondLog.class;
		} else if(query.getResourceType() == 3) {//
			
		} else if(query.getResourceType() == 4) {//木材
			clazz = WoodLog.class;
		} else if(query.getResourceType() == 5) {//肉
			clazz = MeatLog.class;
		} else if(query.getResourceType() == 6) {//矿石
			clazz = MineLog.class;
		}
		Asserts.isTrue(clazz != null, TipsCode.CONSUME_TYPE_NOT_FOUND);
		
		LogTable logTable = clazz.getAnnotation(LogTable.class);
		
		Set<String> queryYearMonth = new HashSet<>();
		long currTime = startTime;
		while(currTime <= endTime) {
			String yearMonth = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMM, currTime);
			String tableName = logTable.name() + "_" + yearMonth;
			
			if(!queryYearMonth.contains(tableName)) {
				if(ReadOnlyDao.isTableExist(dataBase, tableName)) {
					queryYearMonth.add(tableName);
				}
			}
			//步进20天可以统计出从开始时间到结束时间内的各个月份的数据
			currTime += DateTimeUtil.ONE_DAY_MILLIS * 20;
		}
		String sql = "";
		List<String> queryTables = new ArrayList<>(queryYearMonth);
		Collections.sort(queryTables);
		
		List<Object> paramList = new ArrayList<>();
		for(int i = 0; i < queryTables.size(); i++) {
			String tableName = queryTables.get(i);
			if(i != 0) {
				sql += " union all ";
			}
			if (query.getConsumeType() == 1) {
				// 消耗
				sql += "select game_cause, owner_id, delta from "
						+ tableName + " where insert_time >= ? and insert_time <= ? and delta < 0";
			} else {
				// 产出
				sql += "select game_cause, owner_id, delta from "
						+ tableName + " where insert_time >= ? and insert_time <= ? and delta > 0";
			}
			paramList.add(startStr);
			paramList.add(endStr);
		}
		
		String querySql = "select t.game_cause as gameCause, count(DISTINCT t.owner_id) as playerNum, count(1) as countNum, sum(t.delta) as total from (" + sql + ") t group by t.game_cause";
		List<ConsumeData> dataList = ReadOnlyDao.queryAliasObjects(dataBase, ConsumeData.class, querySql, paramList.toArray());

		Map<Long, GameCause> gameCauseMap = ListMapUtil.listToMap(gameCauseService.getAll(), GameCause::getId);
		
		List<ConsumeLogVo> result = new ArrayList<>();
		long allTotal = 0;
		for(int i = 0; i < dataList.size(); i++) {
			ConsumeData data = dataList.get(i);
			allTotal += data.getTotal();
			
			GameCause gameCause = gameCauseMap.get((long)data.getGameCause());
			
			ConsumeLogVo vo = new ConsumeLogVo();
			vo.setIndex(i + 1);
			vo.setGameCause(data.getGameCause());
			vo.setGameCauseTxt(gameCause.getName());
			vo.setPlayerNum(data.getPlayerNum());
			vo.setCountNum(data.getCountNum());
			vo.setTotal(data.getTotal());
			vo.setResourceType(query.getResourceType() + "");
			vo.setConsumeType(query.getConsumeType());
			vo.setAvg(data.getTotal() * 1.0f / data.getPlayerNum());
			result.add(vo);
		}
		
		for(ConsumeLogVo vo : result) {
			if(allTotal != 0) {
				vo.setWeight(vo.getTotal() * 1.0f / allTotal);
			}
		}
		
		PageData<ConsumeLogVo> pageData = new PageData<>();
		pageData.setData(result);
		pageData.setCount(result.size());
		return pageData;
	}
}
