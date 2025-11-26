/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import com.xiugou.x1.backstage.module.player.struct.PlayerTimeLogQuery;
import com.xiugou.x1.backstage.module.player.vo.PlayerTimeVo;

import pojo.xiugou.x1.pojo.log.player.PlayerTimeLog;

/**
 * @author YY
 *
 */
@Controller
public class PlayerTimeLogController {

	@Autowired
	private DataBaseManager dataBaseManager;
	@Autowired
	private GameServerService gameServerService;
	
	@ApiDocument("获取注册分时统计数据")
    @RequestMapping(value = "/playerTimeLog/data.auth")
    @ResponseBody
	public PageData<PlayerTimeVo> data(PlayerTimeLogQuery query) {
		long time = query.getTime() * 1000L;
		if(time <= 0) {
			time = DateTimeUtil.currMillis();
		}
		String dateStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD, time);
		
		QuerySet querySet = new QuerySet();
		querySet.addCondition("date_str = ?", dateStr);
		querySet.formWhere();
		
		Table table = PlayerTimeLog.class.getAnnotation(Table.class);
		
		List<PlayerTimeLog> results = new ArrayList<>();
		for(int serverUid : query.getServerUids()) {
			GameServer gameServer = gameServerService.getEntity(serverUid);
			if(gameServer == null) {
				continue;
			}
			DataBase dataBase = dataBaseManager.getLogDb(serverUid);
			try {
				results.addAll(ReadOnlyDao.queryObjects(dataBase, PlayerTimeLog.class, table.name(), querySet.getWhere(), querySet.getParams()));
			} catch (Exception e) {
			}
		}
		Map<String, List<PlayerTimeLog>> periodMap = ListMapUtil.fillListMap(results, PlayerTimeLog::getTimePeriod);
		
		List<String> timePeriods = new ArrayList<>(periodMap.keySet());
		Collections.sort(timePeriods);
		
		List<PlayerTimeVo> voList = new ArrayList<>();
		for(String timePeriod : timePeriods) {
			List<PlayerTimeLog> logList = periodMap.get(timePeriod);
			if(logList == null) {
				continue;
			}
			PlayerTimeVo vo = new PlayerTimeVo();
			vo.setTimePeriod(dateStr + " " + timePeriod);
			for(PlayerTimeLog log : logList) {
				vo.setCreateNum(vo.getCreateNum() + log.getCreateNum());
				vo.setLoginNum(vo.getLoginNum() + log.getLoginNum());
				vo.setMaxOnline(vo.getMaxOnline() + log.getMaxOnline());
				vo.setMinOnline(vo.getMinOnline() + log.getMinOnline());
			}
			voList.add(vo);
		}
		
		SortUtil.sortStr(voList, PlayerTimeVo::getTimePeriod);

		PageData<PlayerTimeVo> pageData = new PageData<>();
		pageData.setCount(voList.size());
		pageData.setData(voList);
		return pageData;
	}
}
