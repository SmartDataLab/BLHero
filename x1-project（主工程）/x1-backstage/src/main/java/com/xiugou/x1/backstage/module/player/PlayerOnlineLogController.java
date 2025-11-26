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
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.player.struct.PlayerOnlineLogQuery;
import com.xiugou.x1.backstage.module.player.vo.PlayerOnlineVo;

import pojo.xiugou.x1.pojo.log.player.PlayerOnlineLog;

/**
 * @author YY
 *
 */
@Controller
public class PlayerOnlineLogController {

	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("获取在线人数数据")
    @RequestMapping(value = "/playerOnlineLog/data.auth")
    @ResponseBody
	public PageData<PlayerOnlineVo> data(PlayerOnlineLogQuery query) {
		long startTime = query.getTime() * 1000L;
		if(startTime <= 0) {
			startTime = DateTimeUtil.currMillis();
		}
		String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, startTime);
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("date_str = ?", dateStr);
		querySet.formWhere();
		
		Table table = PlayerOnlineLog.class.getAnnotation(Table.class);
		
		List<PlayerOnlineLog> results = new ArrayList<>();
		for(int serverUid : query.getServerUids()) {
			GameServer gameServer = gameServerService.getEntity(serverUid);
			if(gameServer == null) {
				continue;
			}
			DataBase dataBase = dataBaseManager.getLogDb(serverUid);
			try {
				results.addAll(ReadOnlyDao.queryObjects(dataBase, PlayerOnlineLog.class, table.name(), querySet.getWhere(), querySet.getParams()));
			} catch (Exception e) {
			}
		}
		
		Map<Integer, List<PlayerOnlineLog>> datePeriodMap = ListMapUtil.fillListMap(results, PlayerOnlineLog::getTimePeriod);
		
		List<PlayerOnlineVo> voList = new ArrayList<>();
		for(Entry<Integer, List<PlayerOnlineLog>> entry : datePeriodMap.entrySet()) {
			int period = entry.getKey();
			PlayerOnlineVo vo = new PlayerOnlineVo();
			vo.setPeriod(period);
			long time = DateTimeUtil.todayZeroMillis() + period * (DateTimeUtil.ONE_MINUTE_MILLIS * 30);
			vo.setPeriodText(DateTimeUtil.formatMillis(DateTimeUtil.HH_MM, time));
			for(PlayerOnlineLog log : entry.getValue()) {
				vo.setOnlineNum(vo.getOnlineNum() + log.getOnlineNum());
				vo.setNewOnlineNum(vo.getNewOnlineNum() + log.getNewOnlineNum());
			}
			voList.add(vo);
		}
		
		SortUtil.sortInt(voList, PlayerOnlineVo::getPeriod);

		PageData<PlayerOnlineVo> pageData = new PageData<>();
		pageData.setCount(voList.size());
		pageData.setData(voList);
		return pageData;
	}
	
}
