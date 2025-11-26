/**
 * 
 */
package com.xiugou.x1.backstage.module.player;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;
import com.xiugou.x1.backstage.module.player.struct.ServerResumeQuery;

import pojo.xiugou.x1.pojo.log.server.ServerResumeLog;

/**
 * @author YY
 *
 */
@Controller
public class ServerResumeController {
	
	@Autowired
	private DataBaseManager dataBaseManager;
	
	@ApiDocument("获取游戏汇总数据")
    @RequestMapping(value = "/serverResume/data.auth")
    @ResponseBody
	public PageData<ServerResumeLog> data(ServerResumeQuery query) {
		DataBase dataBase = dataBaseManager.getLogDb(query.getServerUid());
		
		QuerySet querySet = new QuerySet();
		
		long startTime = query.getStartTime() * 1000L;
		if(startTime > 0) {
			String startStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, startTime);
			querySet.addCondition("date_str >= ?", startStr);
		}
		
		long endTime = query.getEndTime() * 1000L;
		if(endTime > 0) {
			String endStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMMDD, endTime);
			querySet.addCondition("date_str <= ?", endStr);
		}
		querySet.orderBy("order by date_str desc");
		querySet.formWhere();
		
		Table table = ServerResumeLog.class.getAnnotation(Table.class);
		List<ServerResumeLog> log = ReadOnlyDao.queryObjects(dataBase, ServerResumeLog.class, table.name(), querySet.getWhere(), querySet.getParams());
		
		PageData<ServerResumeLog> pageData = new PageData<>();
		pageData.setCount(log.size());
		pageData.setData(log);
		return pageData;
	}
}
