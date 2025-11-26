/**
 * 
 */
package org.gaming.backstage.module.user.service;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.interceptor.RoleContext;
import org.gaming.backstage.module.user.model.UserLog;
import org.gaming.backstage.service.AbstractService;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.stereotype.Service;

/**
 * @author YY
 *
 */
@Service
public class UserLogService extends AbstractService<UserLog> {

	public PageData<UserLog> queryLogs(String tableName, QuerySet querySet) {
		List<UserLog> list = this.repository().getBaseDao().queryObjects(tableName, querySet.getWhere(), querySet.getParams());
		
		PageData<UserLog> pageData = new PageData<>();
		pageData.setCount(this.repository().getBaseDao().count(tableName, querySet.getCountWhere(), querySet.getCountParams()));
		pageData.setData(list);
		return pageData;
	}
	
	public void addLog(RoleContext roleContext, String requestUrl, String param) {
		UserLog log = new UserLog();
		log.setUserId(roleContext.getId());
		log.setUserName(roleContext.getName());
		log.setRequestUrl(requestUrl);
		log.setParam(param);
		this.repository().insert(log);
	}
}
