/**
 * 
 */
package com.xiugou.x1.backstage.module.gamelog.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.mysql.dao.ReadOnlyDao;
import org.gaming.db.mysql.database.DataBase;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gamelog.struct.LogQuery;
import com.xiugou.x1.backstage.module.gameserver.service.DataBaseManager;

/**
 * @author YY
 *
 */
@Service
public class LogTableService {

	@Autowired
	private DataBaseManager dataBaseManager;
	
	public <T extends AbstractEntity> PageData<T> queryDatas(Class<T> clazz, LogQuery query) {
		LogTable logTable = clazz.getAnnotation(LogTable.class);
		if(logTable == null) {
			return new PageData<>();
		}
		
		int serverUid = query.getServerUid();
		Asserts.isTrue(serverUid > 0, TipsCode.GAME_SERVER_MISS, serverUid);
		
		DataBase dataBase = dataBaseManager.getLogDb(serverUid);
		int yearMonthTime = query.getStartTime();
		if(yearMonthTime <= 0) {
			yearMonthTime = DateTimeUtil.currSecond();
		}
		
		String yearMonth = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMM, yearMonthTime * 1000L);
		String tableName = logTable.name() + "_" + yearMonth;
		
		QuerySet querySet = new QuerySet();
		if(query.getPlayerId() > 0) {
			querySet.addCondition("owner_id = ?", query.getPlayerId());
		}
		if(query.getStartTime() > 0) {
			String startTimeStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD_HH_MM_SS, query.getStartTime() * 1000L);
			querySet.addCondition(logTable.byColumn() + " >= ?", startTimeStr);
		}
		if(query.getEndTime() > 0) {
			String endTimeStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD_HH_MM_SS, query.getEndTime() * 1000L + DateTimeUtil.ONE_DAY_MILLIS);
			querySet.addCondition(logTable.byColumn() + " <= ?", endTimeStr);
		}
		if(query.getCause() > 0) {
			querySet.addCondition("game_cause = ?", query.getCause());
		}
		
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		List<T> results = ReadOnlyDao.queryObjects(dataBase, clazz, tableName, querySet.getWhere(), querySet.getParams());

		PageData<T> pageData = new PageData<>();
		pageData.setCount(ReadOnlyDao.count(dataBase, tableName, querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(results);
		return pageData;
	}
}
