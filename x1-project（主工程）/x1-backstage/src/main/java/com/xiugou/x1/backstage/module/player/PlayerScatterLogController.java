/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.GsonUtil;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.struct.PlayerScatterLogQuery;
import com.xiugou.x1.backstage.module.player.vo.PlayerScatterVo;

import pojo.xiugou.x1.pojo.log.player.PlayerScatterLog;

/**
 * @author YY
 *
 */
@Controller
public class PlayerScatterLogController {

	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("获取玩家在线时间占比")
    @RequestMapping(value = "/playerScatterLog/data.auth")
    @ResponseBody
    public PageData<PlayerScatterVo> data(PlayerScatterLogQuery query) {
		System.out.println(GsonUtil.toJson(query));
		QuerySet querySet = new QuerySet();
		long startTime = query.getStartTime() * 1000L;
		if(startTime <= 0) {
			startTime = DateTimeUtil.currMillis() - DateTimeUtil.ONE_DAY_MILLIS;
		}
		long endTime = query.getEndTime() * 1000L;
		if(endTime <= 0) {
			endTime = DateTimeUtil.currMillis();
		}
		querySet.addCondition("date >= ?", DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, startTime));
		querySet.addCondition("date <= ?", DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime));
		querySet.formWhere();
		
		Table table = PlayerScatterLog.class.getAnnotation(Table.class);
		
		List<PlayerScatterLog> results = new ArrayList<>();
		for(int serverUid : query.getServerUids()) {
			GameServer gameServer = gameServerService.getEntity(serverUid);
			if(gameServer == null) {
				continue;
			}
			DataBase dataBase = dataBaseManager.getLogDb(serverUid);
			try {
				results.addAll(ReadOnlyDao.queryObjects(dataBase, PlayerScatterLog.class, table.name(), querySet.getWhere(), querySet.getParams()));
			} catch (Exception e) {
			}
		}
		Map<Integer, Map<Integer, List<PlayerScatterLog>>> datePeriodMap = ListMapUtil.fillTwoListMap(results, PlayerScatterLog::getDate, PlayerScatterLog::getTimePeriod);
		
		List<PlayerScatterVo> voList = new ArrayList<>();
		for(Entry<Integer, Map<Integer, List<PlayerScatterLog>>> outEntry : datePeriodMap.entrySet()) {
			int date = outEntry.getKey();
			for(Entry<Integer, List<PlayerScatterLog>> inEntry : outEntry.getValue().entrySet()) {
				int period = inEntry.getKey();
				PlayerScatterVo vo = new PlayerScatterVo();
				vo.setDate(date);
				vo.setTimePeriod(period);
				for(PlayerScatterLog log : inEntry.getValue()) {
					vo.setTimePeriodText(log.getTimePeriodText());
					vo.setOnlineNum(vo.getOnlineNum() + log.getOnlineNum());
					vo.setOnlineBase(vo.getOnlineBase() + log.getOnlineBase());
				}
				voList.add(vo);
			}
		}
		
		SortUtil.sortInt(voList, PlayerScatterVo::getDate, PlayerScatterVo::getTimePeriod);
		
		PageData<PlayerScatterVo> pageData = new PageData<>();
		pageData.setCount(voList.size());
		pageData.setData(voList);
		return pageData;
    }
}
