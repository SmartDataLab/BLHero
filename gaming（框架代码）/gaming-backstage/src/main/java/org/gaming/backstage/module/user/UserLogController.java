/**
 * 
 */
package org.gaming.backstage.module.user;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.backstage.module.user.model.UserLog;
import org.gaming.backstage.module.user.service.UserLogService;
import org.gaming.backstage.module.user.struct.UserLogQuery;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author YY
 *
 */
@Controller
public class UserLogController {
	
	@Autowired
	private UserLogService userLogService;

	@ApiDocument("请求用户操作日志")
	@RequestMapping(value = "/userlog/data.auth")
	@ResponseBody
	public PageData<UserLog> userLog(UserLogQuery query) {
		LogTable logTable = UserLog.class.getAnnotation(LogTable.class);
		
		int yearMonthTime = query.getStartTime();
		if(yearMonthTime <= 0) {
			yearMonthTime = DateTimeUtil.currSecond();
		}
		String yearMonth = DateTimeUtil.formatMillis(DateTimeUtil.YYYYMM, yearMonthTime * 1000L);
		String tableName = logTable.name() + "_" + yearMonth;
		
		QuerySet querySet = new QuerySet();
		if(query.getUserName() != null && !"".equals(query.getUserName())) {
			querySet.addCondition("user_name like ?", "%" + query.getUserName() + "%");
		}
		if(query.getStartTime() > 0) {
			String startTimeStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD_HH_MM_SS, query.getStartTime() * 1000L);
			querySet.addCondition(logTable.byColumn() + " >= ?", startTimeStr);
		}
		if(query.getEndTime() > 0) {
			String endTimeStr = DateTimeUtil.formatMillis(DateTimeUtil.YYYY_MM_DD_HH_MM_SS, query.getEndTime() * 1000L);
			querySet.addCondition(logTable.byColumn() + " <= ?", endTimeStr);
		}
		querySet.orderBy("order by id desc");
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return userLogService.queryLogs(tableName, querySet);
	}
}
